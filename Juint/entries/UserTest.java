package entries;

import app.entries.User;


public class UserTest {
	public static void main(String[] args){
		User user = new User();
		
		user.setUsername("Test");
		
		System.out.println(user);
	}
}
