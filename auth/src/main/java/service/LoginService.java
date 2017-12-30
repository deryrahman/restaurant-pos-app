package service;

import exception.DataNotFoundException;
import exception.InvalidCredentialsException;
import model.UserIdentity;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class LoginService {
    public static boolean isValidCredential(String username, String password) throws InvalidCredentialsException, DataNotFoundException, SQLException {
        UserIdentity user = UserIdentityService.findByUsername(username);
        if (BCrypt.checkpw(password, user.getPassword())) {
            return true;
        } else {
            throw new InvalidCredentialsException("Wrong password.");
        }
    }
}
