package Excercise3.Exception;

public class InvalidEmailException extends Exception {
    private String invalidEmail;
    public InvalidEmailException(String msg, String invalidEmail) {
        super(msg);
        this.invalidEmail = invalidEmail;
    }

    public String getInvalidEmail() {
        return invalidEmail;
    }
}
