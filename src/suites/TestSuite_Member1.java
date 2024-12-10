package suites;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import testing.GetAllWordsTesting;
import testing.GetFavoriteWordsTesting;
import testing.MarkFavoriteTesting;
import testing.NormalizationTesting;
import testing.NormalizeWordsUsingJar;
import testing.ViewOnceTesting;

@Suite
@SelectClasses({ ViewOnceTesting.class, GetAllWordsTesting.class, GetFavoriteWordsTesting.class,
		MarkFavoriteTesting.class, NormalizationTesting.class, NormalizeWordsUsingJar.class })


@Execution(ExecutionMode.CONCURRENT)
public class TestSuite_Member1 {
	//3709
}
