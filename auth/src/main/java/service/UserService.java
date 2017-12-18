package service;

import model.UserIdentity;
import model.UserIdentityDAO;

import java.util.List;

public class UserService {
    private static UserIdentityDAO userIdentityDAO = new UserIdentityDAO();
    private static List<UserIdentity> userIdentities = userIdentityDAO.getAllUserIdentity();

    public static boolean isValid(String username, String password) {
        return getUserIdentity(username).getPassword().equals(password);
    }

    public static UserIdentity getUserIdentity(String username) {
        for (UserIdentity user : userIdentities) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return new UserIdentity();
    }

    public static String getRole(String username) {
        return getUserIdentity(username).getRole();
    }
}
