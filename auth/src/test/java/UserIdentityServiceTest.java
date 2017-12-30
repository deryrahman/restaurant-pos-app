import exception.DataNotFoundException;
import model.uid.UserIdentity;
import service.UserIdentityService;

import java.sql.SQLException;
import java.util.List;

public class UserIdentityServiceTest {
    public static void main(String[] args) throws SQLException, DataNotFoundException {
        getAllTest();
        findByIdTest(2L);
        findByUsernameTest("iqbal");
    }

    private static void findByUsernameTest(String username) throws SQLException, DataNotFoundException{
        UserIdentity uid = UserIdentityService.findByUsername(username);
        System.out.println(uid);
    }

    private static void getAllTest() throws SQLException {
        List<UserIdentity> userIdentities = UserIdentityService.getAll();
        for (UserIdentity uid :
                userIdentities) {
            System.out.println(uid);
        }
    }

    private static void findByIdTest(long id) throws SQLException, DataNotFoundException {
        UserIdentity uid = UserIdentityService.findById(id);
        System.out.println(uid);
    }
}
