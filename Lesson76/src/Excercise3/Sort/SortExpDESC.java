package Excercise3.Sort;

import Excercise3.Lecturer;
import Excercise3.Person;

import java.util.Comparator;

public class SortExpDESC implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        if(o1 instanceof Lecturer && o2 instanceof Lecturer) {
            var item1 = (Lecturer) o1;
            var item2 = (Lecturer) o2;
            return (item1.getExpYear() < item2.getExpYear()) ? 1 : -1;
        }
        return -1;
    }
}
