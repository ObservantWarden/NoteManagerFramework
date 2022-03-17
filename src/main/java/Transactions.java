import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс <code>Transactions</code> содержит прототипы
 * запросов к БД.
 */
public class Transactions {

    public static void sendNote(Connection con, String text) throws  SQLException{
        ResultSet rs = con.createStatement().executeQuery("select max(id) from notes;");
        rs.next();
        int id = rs.getInt(1) + 1;
        String command = "INSERT INTO Pizda (id, text) values (" + id + ", '" + text + "');";
        con.createStatement().execute(command);
        con.close();
    }

    @Nullable
    public static String getNote(Connection con, int id) throws SQLException{
        ResultSet rs = con.createStatement().executeQuery("SELECT text FROM notes WHERE id = " + id + ";");
        rs.next();
        String result = rs.getString("text");
        con.close();
        return result;
    }
}