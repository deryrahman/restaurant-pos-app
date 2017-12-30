package service;

import exception.DataNotFoundException;
import model.UserIdentity;
import model.UserIdentityDAO;

import java.sql.SQLException;
import java.util.List;

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

    public static long createNew(UserIdentity newUserIdentity) throws SQLException {
        userIdentityDAO.create(newUserIdentity);
        return newUserIdentity.getId();
    }

    public static long update(UserIdentity userIdentity) throws SQLException {
        userIdentityDAO.update(userIdentity);
        return userIdentity.getId();
    }

    public static void delete(long id) throws SQLException {
        userIdentityDAO.delete(id);
    }

}
