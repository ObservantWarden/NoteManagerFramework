import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

public class User
{
    private final UUID  userId;
    private final String userName;
    private final String userPassword;
    private final Connection sqlCon;

    public User (String userName, String userPassword, UUID userId, Connection connection)
    {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userId = userId;
        sqlCon = connection;
    }

    public UUID getUserId ()
    {
        return userId;
    }

    private PictureNote getPicture (UUID id)
    {
        String command = "SELECT image FROM ImageHolder WHERE id_image = '" + id.toString() + "';";
        ResultSet rs;
        try
        {
            rs = sqlCon.createStatement().executeQuery(command);
            rs.next();
            return new PictureNote(rs.getBytes(1));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private TextNote getText (UUID id)
    {
        String command = "SELECT text FROM TextHolder WHERE id_text = '" + id.toString() + "';";
        ResultSet rs;
        try
        {
            rs = sqlCon.createStatement().executeQuery(command);
            rs.next();
            return new TextNote(rs.getString(1));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Vector<INote> getUserData ()
    {
        Vector<INote> data = new Vector<>();
        Vector<UUID> uuidsText = new Vector<>();
        Vector<UUID> uuidsImages = new Vector<>();
        String command = "SELECT id_text, id_image FROM IdHolder WHERE id_user = '" + userId.toString() + "';";
        System.out.println(command);
        try {
            ResultSet rs = sqlCon.createStatement().executeQuery(command);
            while(rs.next())
            {
                if(rs.getString(1) != null)
                    uuidsText.add(UUID.fromString(rs.getString(1)));
                if(rs.getString(2) != null)
                    uuidsImages.add(UUID.fromString(rs.getString(2)));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        for (UUID uuid : uuidsText)
            data.add(getText(uuid));
        for(UUID uuid: uuidsImages)
            data.add(getPicture(uuid));
        return data;
    }
}
