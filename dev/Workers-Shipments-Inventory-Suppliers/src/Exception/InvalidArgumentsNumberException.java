package Exception;

public class InvalidArgumentsNumberException extends RuntimeException{
    private final String msg;

    public InvalidArgumentsNumberException(String msg){
        this.msg=msg;
    }

    public String getMessage(){
        return this.msg;
    }
}
