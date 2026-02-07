package bl.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

/**
 * Utility class for executing async tasks properly in a Swing application.
 * 
 * This replaces raw Thread usage with a managed ExecutorService that:
 * - Uses daemon threads (won't prevent app shutdown)
 * - Has named threads for easier debugging
 * - Properly marshals results back to the EDT (Event Dispatch Thread)
 * - Handles errors gracefully
 */
public class TaskExecutor {
    private static final Logger LOGGER = Logger.getLogger(TaskExecutor.class.getName());
    
    private static final int THREAD_POOL_SIZE = 4;
    
    private static final ExecutorService executor = Executors.newFixedThreadPool(
        THREAD_POOL_SIZE, 
        new DaemonThreadFactory("DictWorker")
    );
    
    private TaskExecutor() {
        // Private constructor - utility class
    }
    
    /**
     * Run a task asynchronously and handle the result on the EDT.
     * 
     * @param task The task to run in background (should not touch UI)
     * @param onSuccess Callback for successful result (runs on EDT)
     * @param onError Callback for errors (runs on EDT)
     * @param <T> The result type
     */
    public static <T> void runAsync(
            Supplier<T> task, 
            Consumer<T> onSuccess, 
            Consumer<Exception> onError) {
        
        executor.submit(() -> {
            try {
                T result = task.get();
                SwingUtilities.invokeLater(() -> {
                    try {
                        onSuccess.accept(result);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error in success callback", e);
                    }
                });
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Async task failed", e);
                SwingUtilities.invokeLater(() -> {
                    try {
                        onError.accept(e);
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Error in error callback", ex);
                    }
                });
            }
        });
    }
    
    /**
     * Run a task asynchronously without caring about the result.
     * Errors are logged but not propagated.
     * 
     * @param task The task to run in background
     */
    public static void runAsync(Runnable task) {
        executor.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Background task failed", e);
            }
        });
    }
    
    /**
     * Run a task asynchronously with only error handling.
     * 
     * @param task The task to run
     * @param onError Callback for errors (runs on EDT)
     */
    public static void runAsync(Runnable task, Consumer<Exception> onError) {
        executor.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Background task failed", e);
                SwingUtilities.invokeLater(() -> onError.accept(e));
            }
        });
    }
    
    /**
     * Run a task asynchronously with completion callback.
     * 
     * @param task The task to run
     * @param onComplete Callback when done (runs on EDT, receives success boolean)
     */
    public static void runAsyncWithCompletion(Runnable task, Consumer<Boolean> onComplete) {
        executor.submit(() -> {
            boolean success = true;
            try {
                task.run();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Background task failed", e);
                success = false;
            }
            final boolean finalSuccess = success;
            SwingUtilities.invokeLater(() -> onComplete.accept(finalSuccess));
        });
    }
    
    /**
     * Shutdown the executor gracefully.
     * Call this on application exit.
     */
    public static void shutdown() {
        LOGGER.log(Level.INFO, "Shutting down TaskExecutor...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        LOGGER.log(Level.INFO, "TaskExecutor shutdown complete");
    }
    
    /**
     * Custom thread factory that creates daemon threads with descriptive names.
     */
    private static class DaemonThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        
        DaemonThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }
        
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + "-" + threadNumber.getAndIncrement());
            t.setDaemon(true); // Won't prevent JVM shutdown
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
