import java.sql.Connection;

/**
 * Класс <code>Transactions</code> содержит прототипы
 * запросов к БД.
 */
public class Transactions {
    public static void sendNote(Connection con, String text) {
        // TODO:
        // сохранить запись в таблице
    }

    public static String getNote(Connection con, int id) {
        // TODO:
        // получить запись из таблицы по её id
        return null;
    }
}