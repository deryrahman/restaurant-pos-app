import model.response.ResponseBody;
import model.uid.UserIdentity;
import service.UserIdentityService;

import java.util.ArrayList;
import java.util.List;

public class ResponseBodyTest {

    public static void main(String[] args) throws Exception {
        String message = "Test message";
        List<UserIdentity> userIdentities = new ArrayList<>();
        ResponseBody responseBody = new ResponseBody<>(message, userIdentities);

        System.out.println(responseBody);
        System.out.println(responseBody.toJSON());
    }
}
