package Excercise2;


import Excercise2.Exception.InvalidSalaryException;
import Excercise2.Sort.SortBallanceDESC;
import Excercise2.Sort.SortNameAccountASC;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Test2 {
    private static final String USER = "sa";
    private static final String PASS_WORD = "irohas2004";
    private static final String SERVER_NAME = "NGUYENQUANGPHU\\SQLEXPRESS001";
    private static final String DATABASE_NAME = "Lesson76";
    private static final int PORT = 1433;
    public static void main(String[] args)  {
        var input = new Scanner(System.in);
        List<AccountBank> accountBanks = new ArrayList<>();
        accountBanks.addAll(readAccountInDataBase());
        nextId(accountBanks);
        boolean check = true;
        while (check) {
            menu();
            int choice = Integer.parseInt(input.nextLine());
            switch (choice) {
                case 1:
                    var acc = accountBank(input);
                    if(addAccountToDataBase(acc) > 0) {
                        accountBanks.add(acc);
                        System.out.println("Thêm thành công.");
                    } else {
                        System.out.println("Thêm thất bại.");
                    }
                    break;
                case 2:
                    if(setBallance(accountBanks,input) > 0) {
                        System.out.println("Sửa thành công.");
                    } else {
                        System.out.println("Sửa thất bại.");
                    }
                    break;
                case 3:
                    if(removeAccountByNumberCard(accountBanks,input) > 0) {
                        System.out.println("Xóa thành công.");
                    } else {
                        System.out.println("Xóa thất bại.");
                    }
                    break;
                case 4:
                    try {
                        if(recharge(accountBanks,input) > 0 ) {
                            System.out.println("Nạp tiền thành công.");
                        } else {
                            System.out.println("Nạp tiền thất bại.");
                        }
                    } catch (InvalidSalaryException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        if(withDraw(accountBanks,input) > 0) {
                            System.out.println("Rút tiền thành công.");
                        } else {
                            System.out.println("Rút tiền thất bại.");
                        }
                    } catch (InvalidSalaryException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        if(transfer(input, accountBanks) > 0 ) {
                            System.out.println("Chuyển tiền thành công.");
                        } else {
                            System.out.println("Chuyển tiền thất bại.");
                        }
                    } catch (InvalidSalaryException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    sortNameAccountASC(accountBanks);
                    break;
                case 8:
                    sortBallanceDESC(accountBanks);
                    break;
                case 9:
                    showListAccount(accountBanks);
                    break;
                default:
                    check = false;
                    System.out.println("Cảm ơn bạn đã sử dụng.");
            }
        }

    }

    public static void menu() {
        System.out.println("1. Thêm một tài khoản vào danh sách.");
        System.out.println("2. Sửa số dư tài khoản.");
        System.out.println("3. Xóa tài khoản.");
        System.out.println("4. Nạp tiền vào tài khoản.");
        System.out.println("5. Rút tiền khỏi tài khoản.");
        System.out.println("6. Chuyển tiền từ tài khoản này sang tài khoản khác.");
        System.out.println("7. Sắp xếp danh sách tài khoản theo tên chủ thẻ a-z.");
        System.out.println("8. Sắp xếp danh sách tài khoản theo số dư giảm dần.");
        System.out.println("9. Hiển thị danh sách tài khoản hiện có.");
        System.out.println("Khác. Thoát.");
        System.out.println("Mời bạn chọn chức năng:");

    }


    private static int addAccountToDataBase(AccountBank accountBank) {
        var ds = configDataSource();
        try(Connection conn = ds.getConnection()) {
            String sql = "INSERT INTO Bank_Account(Id,NumberCard,NumberAccount," +
                    "NameAccount,Ballance)" +
                    "VALUES(?,?,?,?,?)";
            var preparedStm = conn.prepareStatement(sql);
            preparedStm.setString(1,accountBank.getIdAccount());
            preparedStm.setString(2,accountBank.getNumberCard());
            preparedStm.setString(3,accountBank.getNumberAccount());
            preparedStm.setString(4,accountBank.getNameAccount());
            preparedStm.setInt(5,accountBank.getBallance());
            return preparedStm.executeUpdate();
        } catch (SQLServerException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    private static List<AccountBank> readAccountInDataBase() {
        List<AccountBank> acb = new ArrayList<>();
        var ds = configDataSource();
        try(Connection conn = ds.getConnection()) {
            String sql = "SELECT * FROM Bank_Account";
            var preparedStm = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStm.executeQuery();
            if(resultSet != null) {
                while(resultSet.next()) {
                    String id = resultSet.getString(1);
                    String numberCard = resultSet.getString(2);
                    String numberAccount = resultSet.getString(3);
                    String nameAccount = resultSet.getString(4);
                    int ballance = resultSet.getInt(5);
                    acb.add(new AccountBank(id,numberCard,
                            numberAccount,nameAccount,ballance));
                }
            }
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return acb;
    }

    public static void sortBallanceDESC(List<AccountBank> accountBanks) {
        Collections.sort(accountBanks,new SortBallanceDESC());
        showListAccount(accountBanks);
    }
    public static void sortNameAccountASC(List<AccountBank> accountBanks) {
        Collections.sort(accountBanks,new SortNameAccountASC());
        showListAccount(accountBanks);
    }

    private static SQLServerDataSource configDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser(USER);
        ds.setPassword(PASS_WORD);
        ds.setPortNumber(PORT);
        ds.setServerName(SERVER_NAME);
        ds.setDatabaseName(DATABASE_NAME);
        ds.setEncrypt(false);
        ds.setIntegratedSecurity(false);
        ds.setTrustServerCertificate(false);
        return ds;
    }
    private static void showListAccount(List<AccountBank> accountBanks) {
        System.out.printf("%-15s%-15s%-15s%-25s%-15s\n","Mã tài khoản",
                "Số thẻ","Số tài khoản","Tên tài khoản","Số dư");
        for (var item :
                accountBanks) {
            System.out.printf("%-15s%-15s%-15s%-25s%-15s\n",
                    item.getIdAccount(),item.getNumberCard(),
                    item.getNumberAccount(),item.getNameAccount(),
                    item.getBallance());
        }
    }
    public static int removeAccountByNumberCard(
            List<AccountBank> accountBanks, Scanner input) {
        System.out.println("Nhập số thẻ:");
        String idCard = input.nextLine();
        var idCardInList = findNumberCardToList(accountBanks,idCard);
        if(idCardInList != null) {
            accountBanks.remove(idCardInList);
            return updateCSLD(idCardInList.getNumberCard());
        } else {
            return -1;
        }
    }

    private static int updateCSLD(String numberCard) {
        var ds = configDataSource();
        try(Connection conn = ds.getConnection()) {
            String sql = "DELETE FROM Bank_Account WHERE NumberCard = ?";
            var preparStm = conn.prepareStatement(sql);
            preparStm.setString(1,numberCard);
            return preparStm.executeUpdate();
        } catch (SQLServerException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int recharge(List<AccountBank> accountBanks,
                                Scanner input) throws InvalidSalaryException {
        System.out.println("Nhập số tài khoản:");
        String numberAccount = input.nextLine();
        var numberAccountList = findNumberAccountInList(
                                accountBanks,numberAccount);
        if(numberAccountList != null) {
            System.out.println("Nhập số tiền cần nạp:");
            int amount = Integer.parseInt(input.nextLine());
            numberAccountList.recharge(amount);
            return updateDataBase(numberAccountList.getIdAccount(),
                                    numberAccountList.getBallance());
        }
        return -1;
    }

    private static int updateDataBase(String id, int ballance) {
        var ds = configDataSource();
        try(Connection conn = ds.getConnection()) {
            String sql = "UPDATE Bank_Account SET Ballance = ? WHERE Id = ?";
            var prepareStm = conn.prepareStatement(sql);
            prepareStm.setInt(1,ballance);
            prepareStm.setString(2,id);
            return prepareStm.executeUpdate();
        } catch (SQLServerException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int withDraw(List<AccountBank> accountBanks,
                               Scanner input) throws InvalidSalaryException {
        System.out.println("Nhập số tài khoản:");
        String numberAccount = input.nextLine();
        var numberAccountList = findNumberAccountInList(
                accountBanks, numberAccount);
        if(numberAccountList != null) {
            System.out.println("Nhập số tiên cần rút:");
            int amount = Integer.parseInt(input.nextLine());
            if(numberAccountList.withDraw(amount)) {
                return updateDataBase(numberAccountList.getIdAccount(),
                                numberAccountList.getBallance());

            }
        }
        return -1;

    }


    public static int transfer(Scanner input,
                               List<AccountBank> accountBanks) throws InvalidSalaryException {
        System.out.println("Nhập số tài khoản nguồn:");
        String numberAccountOne = input.nextLine();
        var checkOne = findNumberAccountInList(
                            accountBanks, numberAccountOne);
        if(checkOne != null) {
            System.out.println("Nhập tài khoản đích:");
            String numberAccountTwo = input.nextLine();
            var checkTwo = findNumberAccountInList(
                            accountBanks,numberAccountTwo);
            if(checkTwo != null) {
                System.out.println("Nhập số tiền cần chuyển:");
                int amount = Integer.parseInt(input.nextLine());
                if(checkOne.transfer(checkTwo,amount)) {
                    updateDataBase(
                            checkOne.getIdAccount(),checkOne.getBallance());
                    return updateDataBase(
                            checkTwo.getIdAccount(),checkTwo.getBallance());
                }
            }
        }
        return -1;
    }

    public static int setBallance(List<AccountBank> accountBanks,
                                  Scanner input) {
        System.out.println("Nhập số tài khoản:");
        String numberAccount = input.nextLine();
        var checkAccount = findNumberAccountInList(
                accountBanks,numberAccount);
        if(checkAccount != null) {
            System.out.println("Nhập số dư:");
            int ballance = Integer.parseInt(input.nextLine());
            if(ballance > 0) {
                checkAccount.setBallance(ballance);
                return updateDataBase(checkAccount.getIdAccount(),
                                checkAccount.getBallance());
            }

        }
        return -1;
    }
    private static AccountBank findNumberAccountInList(
            List<AccountBank> accountBanks, String numberAccount) {
        for (var item :
                accountBanks) {
            if (item.getNumberAccount().compareTo(numberAccount) == 0) {
                return item;
            }
            }
        return null;
    }

    public static AccountBank accountBank(Scanner input) {
        AccountBank accountBank = new AccountBank();
        accountBank.setIdAccount();
        System.out.println("Mã tài khoản:");
        System.out.println(accountBank.getIdAccount());
        System.out.println("Số thẻ:");
        String numberCard = input.nextLine();
        System.out.println("Số tài khoản:");
        String numberAccount = input.nextLine();
        System.out.println("Tên tài khoản:");
        String nameAccount = input.nextLine();
        System.out.println("Số dư:");
        int ballance = Integer.parseInt(input.nextLine());
        return new AccountBank(accountBank.getIdAccount(),
                numberCard,numberAccount,nameAccount,ballance);

    }
    public static void nextId(List<AccountBank> accountBanks) {
        if(accountBanks.size() > 0) {
            String id = accountBanks.get(accountBanks.size() - 1).getIdAccount();
            int next = Integer.parseInt(id.substring(3));
            AccountBank accountBank = new AccountBank();
            accountBank.setNext(next + 1);
        }
    }

    private static AccountBank findNumberCardToList(
            List<AccountBank> accountBanks, String idCard) {
        for (var item :
                accountBanks) {
            if (idCard.compareTo(item.getNumberCard()) == 0) {
                return item;
            }
        }
        return null;
    }


}
