enum ValidationErrorsTypeModel
{
    server,
    local
}

public class ValidationExceptionModel extends Exception
{

    public ValidationExceptionModel (ValidationErrorsTypeModel type)
    {
    }
}
