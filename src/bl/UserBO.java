package bl;

import java.util.HashMap;
import java.util.Map;

public class UserBO implements IUserBO {
	private Map<String, String> users;

	public UserBO() {
		users = new HashMap<>();
		users.put("usman", "3709");
		users.put("sarim", "3688");
		users.put("zainab", "3691");
	}

	@Override
	public boolean validateUser(String username, String password) {
		// Mock login - accept any non-empty credentials
		if (username != null && !username.trim().isEmpty() && 
		    password != null && !password.trim().isEmpty()) {
			return true;
		}
		// Also keep original validation for existing users
		return users.containsKey(username) && users.get(username).equals(password);
	}
}