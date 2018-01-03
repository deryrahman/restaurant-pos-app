import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeExample {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedTimestamp = dateFormat.parse("1995-07-31");
        Timestamp timestamp = new Timestamp(parsedTimestamp.getTime() + (24*60*60*1000));
        Date date = new Date();
        date.setTime(timestamp.getTime());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println(formattedDate);
    }
}
