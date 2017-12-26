import exception.FailedCRUDOperationException;
import model.UserIdentity;
import model.UserIdentityDAO;

import java.util.List;

public class UserIdentityDAOTest {
    public static void main(String[] args) throws FailedCRUDOperationException {
        UserIdentityDAO userIdentityDAO = new UserIdentityDAO();

        //testGetAllUserIdentity(userIdentityDAO);
        //testCreateUserIdentity(userIdentityDAO);
        testUpdateUserIdentity(userIdentityDAO);
    }

    private static void testUpdateUserIdentity(UserIdentityDAO userIdentityDAO) throws FailedCRUDOperationException {
        Long id = 3L;
        String username = "test3";
        String password = "test3";
        String role = "cashier";

        UserIdentity newUser = new UserIdentity(id, username, password, role);
        userIdentityDAO.updateUserIdentity(newUser);
    }

    private static void testCreateUserIdentity(UserIdentityDAO userIdentityDAO) throws FailedCRUDOperationException {
        Long id = 4L;
        String username = "test2";
        String password = "test2";
        String role = "cashier";

        UserIdentity newUser = new UserIdentity(id, username, password, role);
        userIdentityDAO.createUserIdentity(newUser);
    }

    private static void testGetAllUserIdentity(UserIdentityDAO userIdentityDAO) {
        List<UserIdentity> identities = userIdentityDAO.getAllUserIdentity();

        for (UserIdentity identity :
                identities) {
            System.out.println(identity);
        }
    }
}
