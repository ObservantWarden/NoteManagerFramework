import org.postgresql.util.PSQLException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

class PictureNote implements INote{

    private byte[] image;
    private final int id;

    public PictureNote(BufferedImage  image, int id) throws IOException {
        this.id = id;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        this.image = baos.toByteArray();
    }

    @Override
    public void commit(Connection sqlConnection) {
        try {
            String command = "INSERT INTO ImageHolder (id_image, image) values (" + id + ", '" + Arrays.toString(image) + "');";
            sqlConnection.createStatement().execute(command);

            command = "INSERT INTO IdHolder (id, id_image) values (" + id + ", '" + id + "');";
            try {
                sqlConnection.createStatement().execute(command);
            }catch (PSQLException e){
                command = "UPDATE IdHolder SET id_image =" + id + " WHERE id=" + id;
                sqlConnection.createStatement().execute(command);
                sqlConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void fetch(Connection sqlConnection, int id) {
        try{
            ResultSet rs = sqlConnection.createStatement().executeQuery("SELECT image FROM ImageHolder WHERE id_image = " + id);
            rs.next();
            image = rs.getBytes(1);
            sqlConnection.close();
        }catch (SQLException e){e.printStackTrace();}
    }

    @Override
    public BufferedImage getContent(Connection sqlConnection) {
        ByteArrayInputStream bais = new ByteArrayInputStream(image);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}