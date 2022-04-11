import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Authorization
{
    private final String password;
    private final String login;
    private final Connection sqlCon;
    private UUID userId;

    public Authorization (String login, String password, Connection connection)
    {
        this.login = login;
        this.password = password;
        sqlCon = connection;
    }

    private void validation () throws ValidationExceptionModel
    {
        localValidation();
        serverValidation();
    }

    public User authorize () throws ValidationExceptionModel {
        validation();
        return new User(login, password, userId, sqlCon);
    }

    private void localValidation () throws ValidationExceptionModel
    {
        isLoginValid();
        isPasswordValid();
    }

    private void serverValidation () throws ValidationExceptionModel
    {
        String command = "SELECT id_user FROM UserHolder WHERE login = ? and password = ?";
//        String command = "SELECT id_user FROM UserHolder WHERE login = '" + login + "'  and password = '" + password + "';";
        try
        {
            PreparedStatement ps = sqlCon.prepareStatement(command);
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
//            ResultSet rs = sqlCon.createStatement().executeQuery(command);
            rs.next();
            String uuid = rs.getString("id_user");
            userId = UUID.fromString(uuid);
        } catch (SQLException e)
        {
            throw new ValidationExceptionModel(ValidationErrorsTypeModel.server);
        }

    }

    public void isPasswordValid () throws ValidationExceptionModel
    {
        if (password.length() < 8)
            throw new ValidationExceptionModel(ValidationErrorsTypeModel.local);
        if (password.toLowerCase().equals(password))
            throw new ValidationExceptionModel(ValidationErrorsTypeModel.local);
    }

    public void isLoginValid () throws ValidationExceptionModel
    {
        for (int i = 0; i < login.length(); i++)
        {
            if (login.charAt(i) == '@')
            {
                for (int j = i + 1; j < login.length(); j++)
                {
                    if (login.charAt(j) == '.' && j != login.length() - 1)
                    {
                        return;
                    }
                }
                throw new ValidationExceptionModel(ValidationErrorsTypeModel.local);
            }
        }
        throw new ValidationExceptionModel(ValidationErrorsTypeModel.local);
    }
}
