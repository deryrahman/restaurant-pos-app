package service;

import model.UserIdentity;
import model.UserIdentityDAO;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class UserValidator {
    private static UserIdentityDAO userIdentityDAO = new UserIdentityDAO();
    private static List<UserIdentity> userIdentities = userIdentityDAO.getAllUserIdentity();

    public static boolean isValid(String username, String password) {
        boolean valid = false;
        Iterator<UserIdentity> userIterator = userIdentities.iterator();
        while (userIterator.hasNext()) {
            UserIdentity user = userIterator.next();
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    valid = true;
                }
            }
        }
        return valid;
    }
}
