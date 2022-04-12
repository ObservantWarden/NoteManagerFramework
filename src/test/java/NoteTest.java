import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class NoteTest {

    UUID userId = UUID.randomUUID();

    @Mock
    Connection connectionMock = mock(Connection.class);

    @Mock
    ResultSet resultSetMock = mock(ResultSet.class);

    @Mock
    Statement statementMock = mock(Statement.class);

    byte[] bytea = {Byte.parseByte("12"), Byte.parseByte("34")};

    @Before
    public void setup() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(any())).thenReturn(resultSetMock);
        when(resultSetMock.getString(any())).thenReturn("other text");
    }

    @Test
    public void commitTest() throws SQLException {
        INote textNote = new TextNote("text");
        textNote.commit(connectionMock, userId);
        verify(statementMock, times(2)).execute(any());
        PictureNote pictureNote = new PictureNote(bytea); //что то из этого получатеся null и дальше не идет
        pictureNote.commit(connectionMock, userId);
        verify(statementMock, times(4)).execute(any());
    }

    @Test
    public void fetchTest(){
        INote textNote = new TextNote("text");
        textNote.fetch(connectionMock, userId);
        assertThat(textNote.getContent(connectionMock), equalTo("other text"));
    }
}
