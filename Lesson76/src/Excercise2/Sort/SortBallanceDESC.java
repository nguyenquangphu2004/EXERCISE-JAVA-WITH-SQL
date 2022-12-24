package Excercise2.Sort;

import Excercise2.AccountBank;

import java.util.Comparator;

public class SortBallanceDESC implements Comparator<AccountBank> {
    @Override
    public int compare(AccountBank o1, AccountBank o2) {
        return (o1.getBallance() < o2.getBallance()) ? 1 : -1;
    }
}
