import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.UserIdentity;

public class UserIdentityTest {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new UserIdentity(1L, "iqbal", "iqbal", "admin"));

        System.out.println(json);
    }
}
