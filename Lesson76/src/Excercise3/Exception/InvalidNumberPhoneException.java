package Excercise3.Exception;

public class InvalidNumberPhoneException extends Exception{
    private String invalidNumber;
    public InvalidNumberPhoneException(String msg, String invalidNumber) {
        super(msg);
        this.invalidNumber = invalidNumber;
    }

    public String getInvalidNumber() {
        return invalidNumber;
    }
}
