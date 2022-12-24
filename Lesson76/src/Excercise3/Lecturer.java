package Excercise3;

import Excercise3.Exception.InvalidEmailException;
import Excercise3.Exception.InvalidIdCardException;
import Excercise3.Exception.InvalidNumberPhoneException;

import java.sql.Date;
import java.text.ParseException;

public class Lecturer extends Person{
    private static int next = 1001;
    private String idLecturer;
    private String specialize;
    private double salary;
    private double expYear;

    public Lecturer() {

    }

    public Lecturer(String idLecturer, String specialize,
                    double salary, double expYear) {
        this.idLecturer = idLecturer;
        this.specialize = specialize;
        this.salary = salary;
        this.expYear = expYear;
    }

    public Lecturer(String idCard, String fullName,
                    String address, Date birthDate,
                    String email, String numberPhone,
                    String idLecturer, String specialize,
                    double salary, double expYear)
            throws InvalidNumberPhoneException,
            InvalidEmailException, InvalidIdCardException {
        super(idCard, fullName, address, birthDate, email, numberPhone);
        this.idLecturer = idLecturer;
        this.specialize = specialize;
        this.salary = salary;
        this.expYear = expYear;
    }

    public  void setNext(int next) {
        Lecturer.next = next;
    }

    public String getIdLecturer() {
        return idLecturer;
    }

    public void setIdLecturer() {
        this.idLecturer = "LEC" + next;
        next ++;
    }

    public String getSpecialize() {
        return specialize;
    }

    public void setSpecialize(String specialize) {
        this.specialize = specialize;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getExpYear() {
        return expYear;
    }

    public void setExpYear(double expYear) {
        this.expYear = expYear;
    }

    @Override
    public void eat() {

    }

    @Override
    public void sleep() {

    }

    @Override
    public void speak() {

    }

    @Override
    public void relax() {

    }
}
