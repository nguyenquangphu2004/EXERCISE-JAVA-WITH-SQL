package Excercise2;

import Excercise2.Exception.InvalidSalaryException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountBank {
    private static int next = 1001;
    private String idAccount;
    private String numberCard;
    private String numberAccount;
    private String nameAccount;
    private int ballance;

    public AccountBank() {}


    public AccountBank(String idAccount, String numberCard,
                       String numberAccount, String nameAccount,
                       int ballance) {
        this.idAccount = idAccount;
        this.numberCard = numberCard;
        this.numberAccount = numberAccount;
        this.nameAccount = nameAccount;
        this.ballance = ballance;
    }

    public  void setNext(int next) {
        AccountBank.next = next;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount() {
        this.idAccount = "ACC" + next;
        next ++;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public String getNameAccount() {
        return nameAccount;
    }

    public void setNameAccount(String nameAccount) {
        this.nameAccount = nameAccount;
    }

    public int getBallance() {
        return ballance;
    }

    public void setBallance(int ballance) {
        this.ballance = ballance;
    }

    public void recharge(int amount) throws InvalidSalaryException {
        String amountStr = amount + "";
        if(helper(amountStr)) {
            ballance += amount;
        } else {
            throw new InvalidSalaryException(msg());
        }

    }


    public boolean withDraw(int amount) throws InvalidSalaryException {
        String amountStr = amount + "";
        if(helper(amountStr) && ballance >= amount) {
            ballance -= amount;
            return true;
        } else {
            throw new InvalidSalaryException(msg());
        }
    }

    public boolean transfer(AccountBank accountBank, int amount) throws InvalidSalaryException {
        String amountStr = amount + "";
        if(helper(amountStr) && ballance >= amount) {
                ballance -= amount;
                accountBank.setBallance(accountBank.getBallance() + amount);
                return true;
        } else {

            throw new InvalidSalaryException(msg());
        }

    }

    public String msg() {
        return "Số tiền không hợp lệ, vui lòng kiểm tra lại";
    }
    public boolean helper(String msg) {
        String regex = "^\\d{1,9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msg);
        return matcher.matches();
    }
}
