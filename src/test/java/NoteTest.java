import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static java.lang.invoke.MethodHandles.catchException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class NoteTest {

    UUID userId = UUID.randomUUID();

    @Mock
    Connection connectionMock = mock(Connection.class);

    @Mock
    ResultSet resultSetMock = mock(ResultSet.class);

    @Mock
    Statement statementMock = mock(Statement.class);

    @Mock
    BufferedImage bufferedImageMock = mock(BufferedImage.class);

    @Mock
    ByteArrayOutputStream baosMock = mock(ByteArrayOutputStream.class);


    @Before
    public void setup() throws SQLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(any())).thenReturn(resultSetMock);
        when(resultSetMock.getString(any())).thenReturn("other text");
//        when(ImageIO.write(eq(bufferedImageMock), anyString(), eq(baosMock))); //что то из этого получатеся null и дальше не идет

    }

    @Test
    public void commitTest() throws SQLException {
        INote textNote = new TextNote("text");
        textNote.commit(connectionMock, userId);
        verify(statementMock, times(2)).execute(any());
//        PictureNote pictureNote = new PictureNote(bufferedImageMock); //что то из этого получатеся null и дальше не идет
//        pictureNote.commit(connectionMock, userId);
//        verify(statementMock, times(2)).execute(any());
    }

    @Test
    public void fetchTest(){
        INote textNote = new TextNote("text");
        textNote.fetch(connectionMock, userId);
        assertThat(textNote.getContent(connectionMock), equalTo("other text"));
    }
}
