package Excercise3.Exception;

public class InvalidIdCardException extends Exception{
    private String invalidIdCard;
    public InvalidIdCardException(String msg, String invalidIdCard) {
        super(msg);
        this.invalidIdCard = invalidIdCard;
    }

    public String getInvalidIdCard() {
        return invalidIdCard;
    }
}
