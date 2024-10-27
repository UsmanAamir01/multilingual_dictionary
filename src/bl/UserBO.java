package bl;

import java.util.HashMap;
import java.util.Map;

public class UserBO {
    private Map<String, String> users;

    public UserBO() {
        users = new HashMap<>();

        users.put("usman", "3709");
        users.put("sarim", "3688");
        users.put("zainab", "3691");
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