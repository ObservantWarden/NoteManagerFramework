import java.sql.Connection;
import java.util.UUID;

interface INote
{
    void commit (Connection sqlConnection, UUID id);
    void fetch (Connection sqlConnection, UUID id);
    Object getContent (Connection sqlConnection);
}
