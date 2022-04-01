import java.sql.Connection;

interface INote {
    void commit(Connection sqlConnection);
    void fetch(Connection sqlConnection, int id);
    Object getContent(Connection sqlConnection);
}