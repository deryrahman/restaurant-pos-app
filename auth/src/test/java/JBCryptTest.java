import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class JBCryptTest {
    public static void main(String[] args) {
        String password = "iqbal";
        String salt = BCrypt.gensalt();
        String hashed = BCrypt.hashpw(password, salt);

        System.out.println("Plain: " + password);
        System.out.println("Salt: " + salt);
        System.out.println("Hashed: " + hashed);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        System.out.println(BCrypt.checkpw(input, hashed));
    }
}
