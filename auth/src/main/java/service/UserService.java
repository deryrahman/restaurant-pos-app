package service;

import exception.DuplicateDataException;
import exception.FailedCRUDOperationException;
import exception.InvalidCredentialsException;
import model.UserIdentity;
import model.UserIdentityDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Objects;

public class UserService {
    private static UserIdentityDAO userIdentityDAO = new UserIdentityDAO();
    private static List<UserIdentity> userIdentities = userIdentityDAO.getAllUserIdentity();

    public static boolean isValid(String username, String password) throws InvalidCredentialsException {
        String hashed = getUserIdentity(username).getPassword();
        return BCrypt.checkpw(password, hashed);
    }

    public static String getRole(String username) {
        try {
            return getUserIdentity(username).getRole();
        } catch (InvalidCredentialsException e) {
            return "";
        }
    }

    public static UserIdentity getUserIdentity(String username) throws InvalidCredentialsException {
        for (UserIdentity user : userIdentities) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new InvalidCredentialsException("Username not found.");
    }

    public static long createUserIdentity(UserIdentity newUserIdentity) throws FailedCRUDOperationException {
        checkDuplicateIdentity(newUserIdentity);

        String hashedPassword = hashPassword(newUserIdentity.getPassword());
        newUserIdentity.setPassword(hashedPassword);

        userIdentityDAO.createUserIdentity(newUserIdentity);
        userIdentities.add(newUserIdentity);
        return newUserIdentity.getId();
    }

    private static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    private static void checkDuplicateIdentity(UserIdentity newUserIdentity) throws DuplicateDataException {
        for (UserIdentity user :
                userIdentities) {
            if (user.getId() == newUserIdentity.getId()) {
                throw new DuplicateDataException("User ID already exists.");
            }

            if (user.getUsername().equals(newUserIdentity.getUsername())) {
                throw new DuplicateDataException("Username already exists.");
            }
        }
    }


    public static UserIdentity updateUserIdentity(UserIdentity newUserIdentity) throws FailedCRUDOperationException {
        userIdentityDAO.updateUserIdentity(newUserIdentity);
        for (UserIdentity user :
                userIdentities) {
            if (user.getId() == newUserIdentity.getId()) {
                user.setUsername(newUserIdentity.getUsername());
                user.setPassword(newUserIdentity.getPassword());
                user.setRole(newUserIdentity.getRole());
                return user;
            }
        }
        return newUserIdentity;
    }
}
