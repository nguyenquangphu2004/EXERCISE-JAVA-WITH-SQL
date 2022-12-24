package Excercise1.Sort;

import Excercise1.Cat;

import java.util.Comparator;

public class SortNameASC implements Comparator<Cat> {
    @Override
    public int compare(Cat o1, Cat o2) {
        String name1 = o1.getPetName().toLowerCase();
        String name2 = o2.getPetName().toLowerCase();
        return name1.compareTo(name2);
    }
}
