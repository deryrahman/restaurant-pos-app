package service;

import exception.DataNotFoundException;
import model.uid.UserIdentity;
import model.uid.UserIdentityDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserIdentityService {
    private static UserIdentityDAO userIdentityDAO = new UserIdentityDAO();
    private static List<UserIdentity> userIdentities;

    public static List<UserIdentity> getAll() throws SQLException {
        userIdentities = userIdentityDAO.getAll();
        return userIdentities;
    }

    public static UserIdentity findById(long id) throws SQLException, DataNotFoundException {
        String filter = "id = " + Long.toString(id);
        return findByFilter(filter);
    }

    public static UserIdentity findByUsername(String username) throws SQLException, DataNotFoundException {
        String filter = "`username` = \"" + username +"\"";
        return findByFilter(filter);
    }

    private static UserIdentity findByFilter(String filter) throws SQLException, DataNotFoundException {
        List<UserIdentity> findResult = userIdentityDAO.find(filter);
        if (findResult.size() == 0) {
            throw new DataNotFoundException("UserIdentity not found. Filter: " + filter);
        }
        return findResult.get(0);
    }

    public static long createFromMap(Map<String, Object> userIdentityMap) throws SQLException {
        return createNew(mapToUserIdentityObject(userIdentityMap));
    }

    public static long createNew(UserIdentity newUserIdentity) throws SQLException {
        userIdentityDAO.create(newUserIdentity);
        return newUserIdentity.getId();
    }

    public static long updateFromMap(Map<String, Object> userIdentityMap) throws SQLException {
        return update(mapToUserIdentityObject(userIdentityMap));
    }

    public static long update(UserIdentity userIdentity) throws SQLException {
        userIdentityDAO.update(userIdentity);
        return userIdentity.getId();
    }

    public static void delete(long id) throws SQLException {
        userIdentityDAO.delete(id);
    }

    private static UserIdentity mapToUserIdentityObject(Map<String, Object> userIdentityMap) {
        long id = (long) userIdentityMap.get("id");
        String username = (String) userIdentityMap.get("username");
        String plainPassword = (String) userIdentityMap.get("password");
        String role = (String) userIdentityMap.get("role");

        return new UserIdentity(id, username, hashPassword(plainPassword), role);
    }

    private static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt);
    }

}
