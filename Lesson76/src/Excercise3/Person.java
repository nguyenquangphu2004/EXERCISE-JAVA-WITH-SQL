package Excercise3;

import Excercise3.Exception.InvalidEmailException;
import Excercise3.Exception.InvalidIdCardException;
import Excercise3.Exception.InvalidNumberPhoneException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Person {
    private String idCard;
    private FullName fullName = new FullName();

    private String address;
    private Date birthDate;
    private String email;
    private String numberPhone;
    public Person() {

    }

    public Person(String idCard, String fullName,
                  String address, Date birthDate,
                  String email, String numberPhone) throws InvalidNumberPhoneException,
            InvalidEmailException, InvalidIdCardException {
        setIdCard(idCard);
        setFullName(fullName);
        this.address = address;
        this.birthDate = birthDate;
        setEmail(email);
        setNumberPhone(numberPhone);
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) throws InvalidIdCardException {
        String regex = "^[A-Z]+\\d{9,13}$";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(idCard);
        if(matcher.matches()) {
            this.idCard = idCard;
        } else {
            this.idCard = "";
            String msg = "Căn cước không hợp lệ, vui lòng kiểm tra lại.";
            throw new InvalidIdCardException(msg, idCard);
        }
    }

    public String getFullName() {
        return fullName.last + " " + fullName.mid + fullName.first;
    }

    public void setFullName(String fullName) {
        var words = fullName.split(" ");
        this.fullName.last = words[0];
        this.fullName.first = words[words.length - 1];
        this.fullName.mid = "";
        for(int i = 1; i < words.length - 1; i++) {
            this.fullName.mid += words[i] + " ";
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
//       String foramt = "dd-MM-yyyy";
//        SimpleDateFormat dateFormat = new SimpleDateFormat(foramt);
//        this.birthDate = dateFormat.parse(birthDate);
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidEmailException {
        String regex = "^[a-z]+(\\w)*(._)*@gmail.com$";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()) {
            this.email = email;
        } else {
            this.email = "";
            String msg = "Email không hợp lệ, vui lòng kiểm tra lại.";
            throw new InvalidEmailException(msg, email);
        }
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) throws InvalidNumberPhoneException {
        String regex = "^(09|10)\\d{8}$";
        Pattern pattern  = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(numberPhone);
        if(matcher.matches()) {
            this.numberPhone = numberPhone;
        } else {
            this.numberPhone = "";
            String msg = "Số điện thoại không hợp lệ, vui lòng kiểm tra lại.";
            throw new InvalidNumberPhoneException(msg, numberPhone);
        }

    }
    public abstract void eat();
    public abstract void sleep();
    public abstract void speak();
    public  abstract void relax();

    public class FullName {
        private String mid;
        private String last;
        private String first;
    }
}
