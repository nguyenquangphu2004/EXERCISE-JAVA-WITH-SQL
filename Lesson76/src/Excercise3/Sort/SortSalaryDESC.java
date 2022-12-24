package Excercise3.Sort;

import Excercise3.Lecturer;
import Excercise3.Person;

import java.util.Comparator;
import java.util.List;

public class SortSalaryDESC implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        if(o1 instanceof Lecturer && o2 instanceof Lecturer) {
            var item1 = (Lecturer)o1;
            var item2 = (Lecturer)o2;
            return (item1.getSalary() < item2.getSalary() ) ? 1 : -1;
        }
        return -1;
    }
}
