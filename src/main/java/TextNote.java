import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TextNote implements INote{
    private String text;
    private final int id;

    public TextNote(String text, int id){
        this.text = text;
        this.id = id;
    }

    public TextNote(String text){
        this.text = text;
        this.id = 1;
    }

    @Override
    public void commit(Connection sqlConnection) {
        try {
            String command = "INSERT INTO TextHolder (id_text, text) values (" + id + ", '" + text + "');";
            sqlConnection.createStatement().execute(command);


            command = "INSERT INTO IdHolder (id, id_text) values (" + id + ", '" + id + "');";
            try {
                sqlConnection.createStatement().execute(command);
            }catch (PSQLException e){
                command = "UPDATE IdHolder SET id_text =" + id + " WHERE id=" + id;
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
        try{
            ResultSet rs = sqlConnection.createStatement().executeQuery("SELECT text FROM TextHolder WHERE id_text = " + id);
            rs.next();
            text = rs.getString(1);
            sqlConnection.close();
        }catch (SQLException e){e.printStackTrace();}
    }

    @Override
    public String getContent(Connection sqlConnection) {
        return text;
    }
}
