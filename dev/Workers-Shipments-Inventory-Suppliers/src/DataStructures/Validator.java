package DataStructures;

public class Validator {
    private boolean isValid;

    public Validator() {
        this.isValid = true;
    }
    public void valid() {
        isValid = true;
    }
    public void inValid() {
        isValid = false;
    }

    public boolean isValid() {
        return isValid;
    }
}
