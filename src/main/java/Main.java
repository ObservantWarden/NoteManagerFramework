import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/","postgres", "Faleot123");
//            Transactions.sendNote(conn, "pizzda1");
            System.out.println(Transactions.getNote(conn, 5));
//            Transactions.setAutoIncrementTrue(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
