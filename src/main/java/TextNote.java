import org.postgresql.util.PSQLException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


interface INote {
    void commit(Connection sqlConnection);
    void fetch(Connection sqlConnection, int id);
    Object getContent(Connection sqlConnection);
}

public class TextNote implements INote{
    private final String text;
    public TextNote(String text){
        this.text = text;
    }

    @Override
    public void commit(Connection sqlConnection) {
        try {
            ResultSet rs = sqlConnection.createStatement().executeQuery("select max(id_text) from TextHolder;");
            rs.next();
            int id = rs.getInt(1) + 1;
            String command = "INSERT INTO TextHolder (id_text, text) values (" + id + ", '" + text + "');";
            sqlConnection.createStatement().execute(command);


            rs = sqlConnection.createStatement().executeQuery("select max(id_text) from TextHolder;");
            rs.next();
            int idText = rs.getInt(1);
//            command = "INSERT INTO IdHolder (id, id_text) values (" + id + ", '" + id + "');";
//            sqlConnection.createStatement().execute(command);


            rs = sqlConnection.createStatement().executeQuery("select max(id_text) from IdHolder;");
            rs.next();
            id = rs.getInt(1) + 1;
            command = "INSERT INTO IdHolder (id, id_text) values (" + id + ", '" + idText + "');";
            try {
                sqlConnection.createStatement().execute(command);
            }catch (PSQLException e){
                command = "UPDATE IdHolder SET id_text =" + idText + " WHERE id=" + id;
                sqlConnection.createStatement().execute(command);
                sqlConnection.close();
            }

            sqlConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fetch(Connection sqlConnection, int id) {
        try {
            String command = "UPDATE TextHolder SET text = '" + text + "' WHERE id_text=" + id;
            sqlConnection.createStatement().execute(command);
            sqlConnection.close();
        }catch (SQLException e){e.printStackTrace();}
    }

    @Override
    public String getContent(Connection sqlConnection) {
        return text;
    }
}

class PictureNote implements INote{

    private final byte[] image;

    public PictureNote(BufferedImage image){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.image = byteArrayOutputStream.toByteArray();
    }

    @Override
    public void commit(Connection sqlConnection) {
        try {
            ResultSet rs = sqlConnection.createStatement().executeQuery("select max(id_image) from ImageHolder;");
            rs.next();
            int id = rs.getInt(1) + 1;
            String command = "INSERT INTO ImageHolder (id_image, image) values (" + id + ", '" + Arrays.toString(image) + "');";
            sqlConnection.createStatement().execute(command);


            rs = sqlConnection.createStatement().executeQuery("select max(id_image) from ImageHolder;");
            rs.next();
            int idImage = rs.getInt(1);
            rs = sqlConnection.createStatement().executeQuery("select max(id) from IdHolder;");
            rs.next();
            id = rs.getInt(1) + 1;
            command = "INSERT INTO IdHolder (id, id_image) values (" + id + ", '" + idImage + "');";
            try {
                sqlConnection.createStatement().execute(command);
            }catch (PSQLException e){
                command = "UPDATE IdHolder SET id_image =" + idImage + " WHERE id=" + id;
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
            String command = "UPDATE ImageHolder SET image =" + Arrays.toString(image) + " WHERE id_image=" + id;
            sqlConnection.createStatement().execute(command);
            sqlConnection.close();
        }catch (SQLException e){e.printStackTrace();}
    }

    @Override
    public BufferedImage getContent(Connection sqlConnection) {
        ByteArrayInputStream bais = new ByteArrayInputStream(image);
        BufferedImage result = null;
        try {
            result = ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

