enum ValidationErrorsTypeModel
{
    server,
    local
}

public class ValidationExceptionModel extends Exception
{
    ValidationErrorsTypeModel type;
    public ValidationExceptionModel (ValidationErrorsTypeModel type)
    {
        this.type = type;
    }
}
