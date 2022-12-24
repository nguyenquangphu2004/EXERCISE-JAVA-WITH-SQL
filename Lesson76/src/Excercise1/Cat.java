package Excercise1;

import Excercise1.Exception.InvalidAgeCatException;

public class Cat {
    private static int next = 1001;
    private String id;
    private String featherColor;
    private int age;
    private String favoriteFood;
    private String eyesColor;
    private String petName;


    public Cat() {

    }

    public Cat(String featherColor, int age, String favoriteFood,
               String eyesColor, String petName) throws InvalidAgeCatException {
        this.featherColor = featherColor;
        setAge(age);
        this.favoriteFood = favoriteFood;
        this.eyesColor = eyesColor;
        this.petName = petName;
    }

    public Cat(String id, String featherColor, int age,
               String favoriteFood, String eyesColor,
               String petName) throws InvalidAgeCatException {
        this.id = id;
        this.featherColor = featherColor;
        setAge(age);
        this.favoriteFood = favoriteFood;
        this.eyesColor = eyesColor;
        this.petName = petName;
    }

    public  void setNext(int next) {
        Cat.next = next;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = "CAT" + next;
        next ++;
    }

    public String getFeatherColor() {
        return featherColor;
    }

    public void setFeatherColor(String featherColor) {
        this.featherColor = featherColor;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws InvalidAgeCatException {
        if(age >= 0 && age <= 50) {
            this.age = age;
        } else {
            this.age = 0;
            next -= 1;
            String msg = "Tuổi của mèo không hợp lệ, vui lòng kiểm tra lại.";
            throw new InvalidAgeCatException(msg, age);
        }
    }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public String getEyesColor() {
        return eyesColor;
    }

    public void setEyesColor(String eyesColor) {
        this.eyesColor = eyesColor;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }
}
