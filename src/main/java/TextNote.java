import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class TextNote implements INote
{
    private String text;
    private final UUID id;

    public TextNote (String text)
    {
        this.text = text;
        this.id = UUID.randomUUID();
    }

    @Override
    public void commit (Connection sqlConnection, UUID id_user)
    {
        try {
            UUID otherId = UUID.randomUUID();
            String command = "INSERT INTO TextHolder (id_text, text) values ('" + id + "', '" + text + "');";
            sqlConnection.createStatement().execute(command);

            command = "INSERT INTO IdHolder (id, id_text, id_user) values ('" + otherId + "', '" + id + "' , '" + id_user + "');";
            sqlConnection.createStatement().execute(command);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void fetch(Connection sqlConnection, UUID id)
    {
        try
        {
            ResultSet rs = sqlConnection.createStatement().executeQuery("SELECT text FROM TextHolder WHERE id_text = " + id);
            rs.next();
            text = rs.getString(1);
            sqlConnection.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getContent(Connection sqlConnection) {
        return text;
    }
}
