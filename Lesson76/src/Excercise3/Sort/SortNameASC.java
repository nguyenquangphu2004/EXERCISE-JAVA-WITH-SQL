package Excercise3.Sort;

import Excercise3.Lecturer;
import Excercise3.Person;

import java.util.Comparator;

public class SortNameASC implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        int end1 = o1.getFullName().lastIndexOf(" ");
        int end2 = o2.getFullName().lastIndexOf(" ");
        String name1 = o1.getFullName().substring(end1 + 1);
        String name2 = o2.getFullName().substring(end2 + 1);

        return name1.compareTo(name2);
    }
}
