import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс <code>Transactions</code> содержит прототипы
 * запросов к БД.
 */
public class Transactions {
    public static void sendNote(Connection con, String text) {
        try {
            ResultSet rs = con.createStatement().executeQuery("select max(id) from pizda;");
            rs.next();
            int id = rs.getInt(1) + 1;
            String command = "INSERT INTO Pizda (id, text) values (" + id + ", '" + text + "');";
            con.createStatement().execute(command);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNote(Connection con, int id) {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT text  FROM Pizda WHERE id = " + id + ";");
            rs.next();
            String result = rs.getString(1);
            con.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}