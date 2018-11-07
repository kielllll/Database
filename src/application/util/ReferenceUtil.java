package application.util;

public class ReferenceUtil {
	private static int userID = 0;
	
	public static void setUserID(int userID) {
		ReferenceUtil.userID = userID;
	}
	
	public static int getUserID() {
		return userID;
	}
}
