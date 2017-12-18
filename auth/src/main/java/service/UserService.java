package service;

import exception.InvalidCredentialsException;
import model.UserIdentity;
import model.UserIdentityDAO;

import java.util.List;

public class UserService {
    private static UserIdentityDAO userIdentityDAO = new UserIdentityDAO();
    private static List<UserIdentity> userIdentities = userIdentityDAO.getAllUserIdentity();

    public static boolean isValid(String username, String password) throws InvalidCredentialsException {
        return getUserIdentity(username).getPassword().equals(password);
    }

    public static UserIdentity getUserIdentity(String username) throws InvalidCredentialsException {
        for (UserIdentity user : userIdentities) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new InvalidCredentialsException("Username not found.");
    }

    public static String getRole(String username) {
        try {
            return getUserIdentity(username).getRole();
        } catch (InvalidCredentialsException e) {
            return "";
        }
    }
}
