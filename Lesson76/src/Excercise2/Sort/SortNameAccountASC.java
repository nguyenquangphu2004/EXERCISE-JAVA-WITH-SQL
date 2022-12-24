package Excercise2.Sort;

import Excercise2.AccountBank;

import java.util.Comparator;

public class SortNameAccountASC implements Comparator<AccountBank> {
    @Override
    public int compare(AccountBank o1, AccountBank o2) {
        int end = o1.getNameAccount().lastIndexOf(" ");
        int end1 = o2.getNameAccount().lastIndexOf(" ");
        String name1 = o1.getNameAccount().
                substring(end + 1).toLowerCase();
        String name2 = o2.getNameAccount().
                substring(end1 + 1).toLowerCase();
        return name1.compareTo(name2);

    }
}
