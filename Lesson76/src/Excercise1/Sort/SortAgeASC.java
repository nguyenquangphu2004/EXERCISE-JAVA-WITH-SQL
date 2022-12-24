package Excercise1.Sort;

import Excercise1.Cat;

import java.util.Comparator;

public class SortAgeASC implements Comparator<Cat> {
    @Override
    public int compare(Cat o1, Cat o2) {
        return (o1.getAge() > o2.getAge()) ? 1 : -1;
    }
}
