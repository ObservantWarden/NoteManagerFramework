import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.*;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class AuthorizationTest {
    @Mock
    Connection connectionMock = mock(Connection.class);

    @Mock
    Statement statementMock = mock(Statement.class);

    @Mock
    ResultSet resultSetMock = mock(ResultSet.class);

    @Mock
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);

//    @Mock
//            UUID uuidmock = mock(UUID.class);

    UUID userID = UUID.randomUUID();
    User testUser = new User("login@login.login", "Password12", userID, connectionMock);

    @Before
    public void setup() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(any())).thenReturn(resultSetMock);
//        when(preparedStatementMock.executeQuery(any())).thenReturn(resultSetMock); //не работает. Также ничего не могу сдеалать с preparedstatement, потому что там setString
//        when(UUID.randomUUID()).thenReturn(userID);
//        when(UUID.fromString(anyString())).thenReturn(userID); //не могу провести никакие действия с fromstring и uuid, потому что uuid не может быть mock

    }

    @Test
    public void authorizationTest() throws ValidationExceptionModel {
        Authorization auth = new Authorization("login@login.login", "password", connectionMock);
        assertThrows(ValidationExceptionModel.class, auth::authorize);
        auth = new Authorization("login", "Password12", connectionMock);
        assertThrows(ValidationExceptionModel.class, auth::authorize);
        auth = new Authorization("login@login.login", "Password12", connectionMock);
        User user= auth.authorize();
        assertThat(user, equalTo(testUser));
    }




}
