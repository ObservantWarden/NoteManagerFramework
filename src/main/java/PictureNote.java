import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

class PictureNote implements INote{
    private byte[] image;
    private final UUID id;

    public PictureNote (BufferedImage  image)  {
        this.id = UUID.randomUUID();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
        }catch (IOException e){
            e.printStackTrace();
        }
        this.image = baos.toByteArray();
    }

    public PictureNote (byte[] image){
        this.id = UUID.randomUUID();
        this.image = image;
    }

    @Override
    public void commit (Connection sqlConnection, UUID id_user) {
        try
        {
            UUID otherId = UUID.randomUUID();
            String command = "INSERT INTO ImageHolder (id_image, image) values ('" + id + "', '" + Arrays.toString(image) + "');";
            sqlConnection.createStatement().execute(command);

            command = "INSERT INTO IdHolder (id, id_image, id_user) values ('" + otherId + "', '" + id + "', '" + id_user + "');";
            sqlConnection.createStatement().execute(command);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void fetch (Connection sqlConnection, UUID id)
    {
        try
        {
            ResultSet rs = sqlConnection.createStatement().executeQuery("SELECT image FROM ImageHolder WHERE id_image = " + id);
            rs.next();
            image = rs.getBytes("image");
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage getContent (Connection sqlConnection)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(image);
        try
        {
            return ImageIO.read(bais);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
