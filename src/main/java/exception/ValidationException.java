package exception;

public class ValidationException extends RuntimeException {

    public <T> ValidationException(){
        super();
    }

    public ValidationException(String message){
        super(message);
    }
}
