package Excercise1;

import Excercise1.Exception.InvalidAgeCatException;
import Excercise1.Sort.SortAgeASC;
import Excercise1.Sort.SortAgeDESC;
import Excercise1.Sort.SortNameASC;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.security.spec.RSAOtherPrimeInfo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.CollationElementIterator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
    private static final String USER = "sa";
    private static final String PASSWORD = "irohas2004";
    private static final String SERVER_NAME = "NGUYENQUANGPHU\\SQLEXPRESS001";
    private static final int PORT = 1433;
    private static final String DATABASE_NAME = "Lesson76";
    public static void main(String[] args) {
        var input = new Scanner(System.in);
        boolean check = true;
        List<Cat> cats = new ArrayList<>();
        cats.addAll(readInfoToDataBase());
        nextIdCat(cats);
        while (check) {
            menu();
            int choice = Integer.parseInt(input.nextLine());
            switch (choice) {
                case 1:
                    try {
                        var cat = cat(input);
                        if(addCatToDataBase(cat) > 0) {
                            cats.add(cat);
                            System.out.println("Thêm mới thành công.");
                        } else {
                            System.out.println("Thêm mới thất bại.");
                        }
                    } catch (InvalidAgeCatException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    sortAgeASC(cats);
                    break;
                case 3:
                    sortAgeDESC(cats);
                    break;
                case 4:
                    sortNameASC(cats);
                    break;
                case 5:
                    searchName(cats,input);
                    break;
                case 6:
                    System.out.println("Nhập mã mèo cần xóa:");
                    String id = input.nextLine();
                    if(removeCat(id) > 0) {
                        cats.clear();
                        cats.addAll(readInfoToDataBase());
                        System.out.println("Xóa thành công.");
                    } else {
                        System.out.println("Xóa thất bại.");
                    }
                    break;
                case 7:
                    showListCat(cats);
                    break;
            }

        }

    }

    private static int removeCat(String id) {
        var ds = configDataSource();
        try(Connection conn = ds.getConnection()) {
            String sql = "DELETE Cat WHERE Id = ?";
            var preparedStm = conn.prepareStatement(sql);
            preparedStm.setString(1,id);
            return preparedStm.executeUpdate();
        } catch (SQLServerException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void sortAgeASC(List<Cat> cats) {
        Collections.sort(cats,new SortAgeASC());
        showListCat(cats);
    }
    private static void sortAgeDESC(List<Cat> cats) {
        Collections.sort(cats,new SortAgeDESC());
        showListCat(cats);
    }
    private static void sortNameASC(List<Cat> cats) {
        Collections.sort(cats,new SortNameASC());
        showListCat(cats);
    }

    public static void menu() {
        System.out.println("1. Thêm mới danh sách mèo vào CSDL.");
        System.out.println("2. Sắp xếp mèo theo tuổi tăng dần.");
        System.out.println("3. Sắp xếp mèo theo tuổi giảm dần.");
        System.out.println("4. Sắp xếp mèo theo tên a-z.");
        System.out.println("5. Tìm kiếm mèo theo tên gần đúng(giống Google search).");
        System.out.println("6. Xóa mèo theo mã khỏi CSDL.");
        System.out.println("7. Hiển thị danh sách mèo ra màn hình theo dạng bảng gồm các hàng, cột.");
        System.out.println("Khác. Thoát khỏi chương trình.");
        System.out.println("Mời bạn chọn chức năng:");
    }

    public static Cat cat(Scanner input) throws InvalidAgeCatException {
        Cat cat = new Cat();
        System.out.println("Mã mèo:");
        cat.setId();
        System.out.println(cat.getId());
        System.out.println("Màu lông:");
        String featherColor = input.nextLine();
        System.out.println("Tuổi:");
        int age = Integer.parseInt(input.nextLine());
        System.out.println("Món ăn ưa thích:");
        String favoriteFood = input.nextLine();
        System.out.println("Màu mắt:");
        String eyesColor = input.nextLine();
        System.out.println("Tên gọi:");
        String petName = input.nextLine();
        return new Cat(cat.getId(),featherColor,age,favoriteFood,eyesColor,petName);
    }

    private static List<Cat> readInfoToDataBase() {
        List<Cat> cats = new ArrayList<>();
        var ds = configDataSource();
        try(Connection conn = ds.getConnection()) {
            String sql = "SELECT * FROM dbo.Cat";
            var preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet != null) {
                while (resultSet.next()) {
                    String id = resultSet.getString(1);
                    String featherColor = resultSet.getString(2);
                    int age = resultSet.getInt(3);
                    String favoriteFood = resultSet.getString(4);
                    String eyesColor = resultSet.getString(5);
                    String petName = resultSet.getString(6);
                    cats.add(new Cat(id, featherColor, age, favoriteFood, eyesColor, petName));
                }
            }
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidAgeCatException e) {
            e.printStackTrace();
        }
        return cats;
    }

    private static int addCatToDataBase(Cat cat) {
        var ds = configDataSource();
        try(Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO Cat(Id,FeatherColor,Age,FavoriteFood,EyesColor,PetName)" +
                    "VALUES(?,?,?,?,?,?)";
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,cat.getId());
            preparedStatement.setString(2,cat.getFeatherColor());
            preparedStatement.setInt(3,cat.getAge());
            preparedStatement.setString(4,cat.getFavoriteFood());
            preparedStatement.setString(5,cat.getEyesColor());
            preparedStatement.setString(6,cat.getPetName());
            return preparedStatement.executeUpdate();
        } catch (SQLServerException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void nextIdCat(List<Cat> cats) {
        if(cats.size() > 0) {
            String id = cats.get(cats.size() - 1).getId();
            int next = Integer.parseInt(id.substring(3));
            Cat cat = new Cat();
            cat.setNext(next + 1);
        }
    }
    private static void showListCat(List<Cat> cats) {
        System.out.printf("%-14s%-15s%-13s%-18s%-13s%-14s\n","Mã","Màu lông",
                "Tuổi","Món ăn ưu thích","Màu mắt","Tên gọi");
        for (var item :
                cats) {
            System.out.printf("%-14s%-15s%-13s%-18s%-13s%-14s\n",
                        item.getId(),item.getFeatherColor(),
                        item.getAge(), item.getFavoriteFood(),
                    item.getEyesColor(),item.getPetName());
        }
    }
    private static void searchName(List<Cat> cats, Scanner input) {
        System.out.println("Nhập tên cần tìm:");
        String name = input.nextLine();
        String regex = "^.*" + name + ".*$";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        System.out.printf("%-14s%-15s%-13s%-18s%-13s%-14s\n","Mã","Màu lông",
                "Tuổi","Món ăn ưu thích","Màu mắt","Tên gọi");
        for(int i = 0; i < cats.size(); i++) {
            Matcher matcher = pattern.matcher(cats.get(i).getPetName());
            var item = cats.get(i);
            if(matcher.matches()) {
                System.out.printf("%-14s%-15s%-13s%-18s%-13s%-14s\n",
                        item.getId(),item.getFeatherColor(),
                        item.getAge(), item.getFavoriteFood(),
                        item.getEyesColor(),item.getPetName());
            }
        }
    }
    private static SQLServerDataSource configDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser(USER);
        ds.setPassword(PASSWORD);
        ds.setDatabaseName(DATABASE_NAME);
        ds.setEncrypt(false);
        ds.setIntegratedSecurity(false);
        ds.setTrustServerCertificate(false);
        ds.setPortNumber(PORT);
        ds.setServerName(SERVER_NAME);
        return ds;
    }

}
