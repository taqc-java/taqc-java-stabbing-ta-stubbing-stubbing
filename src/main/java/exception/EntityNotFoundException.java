package exception;

public class EntityNotFoundException extends RuntimeException {

    public <T>EntityNotFoundException(Class<T> clazz, long id){
        super("Doesn't exists "+ clazz.toString() +" with id = " +id);
    }

    public EntityNotFoundException(String message){
        super(message);
    }
}
