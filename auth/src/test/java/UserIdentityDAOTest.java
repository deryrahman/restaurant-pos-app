import model.uid.UserIdentity;
import model.uid.UserIdentityDAO;

import java.sql.SQLException;
import java.util.List;

public class UserIdentityDAOTest {
    public static void main(String[] args) throws SQLException {
        UserIdentityDAO userIdentityDAO = new UserIdentityDAO();

        testGetAllUserIdentity(userIdentityDAO);
        testCreateUserIdentity(userIdentityDAO);
        testUpdateUserIdentity(userIdentityDAO);
        testDeleteUserIdentity(userIdentityDAO);
    }

    private static void testDeleteUserIdentity(UserIdentityDAO userIdentityDAO) throws SQLException {
        userIdentityDAO.delete(16L);
    }

    private static void testUpdateUserIdentity(UserIdentityDAO userIdentityDAO) throws SQLException {
        Long id = 16L;
        String username = "test3";
        String password = "test3";
        String role = "cashier";

        UserIdentity newUser = new UserIdentity(id, username, password, role);
        userIdentityDAO.update(newUser);
    }

    private static void testCreateUserIdentity(UserIdentityDAO userIdentityDAO) throws SQLException {
        Long id = 16L;
        String username = "test2";
        String password = "test2";
        String role = "cashier";

        UserIdentity newUser = new UserIdentity(id, username, password, role);
        userIdentityDAO.create(newUser);
    }

    private static void testGetAllUserIdentity(UserIdentityDAO userIdentityDAO) throws SQLException {
        List<UserIdentity> identities = userIdentityDAO.getAll();

        for (UserIdentity identity :
                identities) {
            System.out.println(identity);
        }
    }
}
