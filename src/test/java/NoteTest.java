import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoteTest {

    @Mock
    Connection connectionMock = mock(Connection.class);

    @Mock
    Statement statementMock = mock(Statement.class);

    @Mock
    ResultSet resultSetMock = mock(ResultSet.class);

    @Mock
    File fileMock = mock(File.class);

    byte[] bytesMock = new byte[1];

    @Before
    public void setup() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(any())).thenReturn(resultSetMock);
        when(resultSetMock.getString(any())).thenReturn("some other text");
        when(resultSetMock.getBytes(any())).thenReturn(bytesMock);
    }

    @Test
    public void commitTest()
    {
        INote textNote = new TextNote("some text", '0');
        INote pictureNote = new PictureNote(fileMock, '0');
        textNote.commit(connectionMock);
        pictureNote.commit(connectionMock);
    }

    @Test
    public void fetchTest()
    {
        INote textNote = new TextNote("some text", '0');
        INote pictureNote = new PictureNote(fileMock, '0');
        textNote.fetch(connectionMock);
        pictureNote.fetch(connectionMock);
        assertThat(textNote.getContent(), equalTo("some other text"));
        assertThat(pictureNote.getContent(), isA(File.class));
    }
}
