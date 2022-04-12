import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

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

    @Spy
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);


    UUID userID = UUID.randomUUID();
    User testUser = new User("login@login.login", "Password12", userID, connectionMock);

    @Before
    public void setup() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(any())).thenReturn(resultSetMock);
        when(connectionMock.prepareStatement(any())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.getString("id_user")).thenReturn(userID.toString());
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
