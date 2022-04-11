import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Класс <code>Transactions</code> содержит прототипы
 * запросов к БД.
 */
public class Transactions
{
    public static void sendNote (Connection con, String text)
    {
        try
        {
            UUID id = UUID.randomUUID();
            String command = "INSERT INTO TextHolder (id_text, text) values ('" + id + "', '" + text + "');";
            con.createStatement().execute(command);
            UUID otherId = UUID.randomUUID();
            command = "INSERT INTO IdHolder (id, id_text) values ('" + otherId + "', '" + id + "');";
            con.createStatement().execute(command);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static String getNote (Connection con, UUID id)
    {
        try
        {
            ResultSet rs = con.createStatement().executeQuery("SELECT text FROM TextHolder WHERE id_text = '" + id + "';");
            rs.next();
            return rs.getString("text");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args){
        String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=Faleot123";
        Connection con = null;
        try {
            con= DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert con != null;
//        Transactions.sendNote(con, "hui23");
        UUID id = UUID.fromString("307b078e-d986-490f-bd3f-5b5ccaecc69d");
        System.out.println(Transactions.getNote(con, id));
    }
}
