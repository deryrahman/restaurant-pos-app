import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.uid.UserIdentity;

public class UserIdentityTest {
    public static void main(String[] args) throws JsonProcessingException {
        Object test = "Test";
        Object test2 = 8;
        String a = test.toString();
        String b = test2.toString();

        System.out.println(a);
        System.out.println(b);
    }
}
