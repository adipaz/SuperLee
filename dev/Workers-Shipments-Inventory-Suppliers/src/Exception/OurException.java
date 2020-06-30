package Exception;

public class OurException extends RuntimeException {
    private final String msg;

    public OurException(String msg){
        this.msg=msg;
    }

    public String getMessage(){
        return this.msg;
    }
}
