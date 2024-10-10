package bl;

import dto.Word;
import java.util.HashMap;
import java.util.Map;

public class UserBO {
    private Map<String, String> users;

    public UserBO() {
        users = new HashMap<>();

        users.put("admin", "password");
        users.put("user1", "pass123");
    }
    public boolean validateUser(String username, String password)
    {
    	if(users.containsKey(username))
    	{
    		if(users.get(username).equals(password)) {
    			return true;
    		}
    	}
    	return false;
    }
}