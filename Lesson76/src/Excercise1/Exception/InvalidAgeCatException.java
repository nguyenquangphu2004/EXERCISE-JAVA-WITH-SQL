package Excercise1.Exception;

public class InvalidAgeCatException extends Exception{
    private int invalidAge;
    public InvalidAgeCatException() {

    }
    public InvalidAgeCatException(String msg, int invalidAge) {
        super(msg);
        this.invalidAge = invalidAge;
    }

    public int getInvalidAge() {
        return invalidAge;
    }
}
