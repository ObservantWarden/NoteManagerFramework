import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class TransactionsTest {

    @Mock
    Connection connectionMock = mock(Connection.class);

    @Mock
    ResultSet resultSetMock = mock(ResultSet.class);

    @Mock
    Statement statementMock = mock(Statement.class);

    @Before
    public void setup() throws SQLException {
        when(statementMock.executeQuery(any())).thenReturn(resultSetMock);
        when(resultSetMock.getString(any())).thenReturn("NoteMock");
        when(connectionMock.createStatement()).thenReturn(statementMock);
    }

    @Test
    public void sendNote() throws SQLException {
        Transactions.sendNote(connectionMock, "Text");
        verify(statementMock, times(1)).execute(any());
    }

    @Test
    public void getNote()
    {
        String functionAnswer = Transactions.getNote(connectionMock, 0);
        assertThat(functionAnswer, equalTo("NoteMock"));
    }
}