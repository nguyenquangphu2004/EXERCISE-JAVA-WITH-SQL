package Excercise3;

import Excercise3.Exception.InvalidEmailException;
import Excercise3.Exception.InvalidIdCardException;
import Excercise3.Exception.InvalidNumberPhoneException;
import Excercise3.Sort.SortExpDESC;
import Excercise3.Sort.SortNameASC;
import Excercise3.Sort.SortSalaryDESC;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test3 {
    private static final String USER = "sa";
    private static final String PASS_WORD = "irohas2004";
    private static final String SERVER_NAME = "NGUYENQUANGPHU\\SQLEXPRESS001";
    private static final String DATABASE_NAME = "Lesson76";
    private static final int PORT = 1433;

    public static void main(String[] args) {
        boolean check = true;
        var input = new Scanner(System.in);
        List<Person> people = new ArrayList<>();
        people.addAll(readLecturerToDataBase());
        setNextId(people);
        while(check) {
            menu();
            int choice = 0;
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            switch (choice) {
                case 1:
                    try {
                        var  per = person(input);
                        if(addLecturerToDataBase(per) > 0) {
                            people.add(per);
                            System.out.println("Thêm mới thành công.");
                        }
                    } catch (InvalidIdCardException
                             | InvalidEmailException
                                | InvalidNumberPhoneException
                                | InputMismatchException
                            | IllegalArgumentException e) {
                        setNextId(people);
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    showList(people);
                    break;
                case 3:
                    searchLecturerById(people,input);
                    break;
                case 4:
                    searchLecturerBySalary(people,input);
                    break;
                case 5:
                    searchLecturerByName(people,input);
                    break;
                case 6:
                    setSalaryById(people,input);
                    break;
                case 7:
                    removeLecturerById(people,input);
                    break;
                case 8:
                    sortNameASC(people);
                    break;
                case 9:
                    sortSalaryDESC(people);
                    break;
                case 10:
                    sortExpDESC(people);
                    break;
                default:
                    check = false;
                    System.out.println("Cảm ơn bạn đã sử dụng chương trình.");
            }


        }
    }

    public static void setNextId(List<Person> people) {
        if(people.size() > 0) {
           var lecturer = (Lecturer)people.get(people.size() - 1);
           String id = lecturer.getIdLecturer();
            int next = Integer.parseInt(id.substring(3));
            Lecturer lec = new Lecturer();
            lec.setNext(next + 1);
        }

    }

    private static void menu() {
        System.out.println("==================================================");
        System.out.println("=     NOTE: Định dạng SINH NHẬT là: yyyy-MM-dd.  =");
        System.out.println("==================================================    ");
        System.out.println("1. Thêm mới giảng viên.");
        System.out.println("2. Hiển thị danh sách giảng viên.");
        System.out.println("3. Tìm kiếm giảng viên theo mã.");
        System.out.println("4. Tìm kiếm giảng viên theo mức lương.");
        System.out.println("5. Tìm kiếm giảng viên theo tên gần đúng.");
        System.out.println("6. Cập nhật lương của giảng viên.");
        System.out.println("7. Xóa giảng viên theo mã.");
        System.out.println("8. Sắp xếp tên giảng viên từ a-z");
        System.out.println("9. Sắp xếp giảng viên theo lương giảm dần.");
        System.out.println("10. Sắp xếp giảng viên theo kinh nghiệm giảm dần.");
        System.out.println("Khác. Thoát.");
        System.out.println("Mời bạn chọn chức năng:");
    }

    public static List<Person> readLecturerToDataBase() {
        List<Person> people = new ArrayList<>();
        var ds = configDataSource();
        try(Connection conn= ds.getConnection()) {
            String sql = "SELECT * FROM Instructor";
            var prepareStm = conn.prepareStatement(sql);
            ResultSet resultSet = prepareStm.executeQuery();
            if(resultSet != null) {
              while(resultSet.next()) {
                  String idCard = resultSet.getString(1);
                  String fullName = resultSet.getString(2);
                  String address = resultSet.getString(3);
                  Date birthDate = resultSet.getDate(4);
                  String email = resultSet.getString(5);
                  String numberPhone = resultSet.getString(6);
                  String idLecturer = resultSet.getString(7);
                  String specialize = resultSet.getString(8);
                  double salary = resultSet.getDouble(9);
                  double expYear = resultSet.getDouble(10);
                  people.add(
                          new Lecturer(idCard,fullName,address,birthDate,email,
                                  numberPhone,idLecturer,specialize,salary,
                                  expYear));
              }
            }
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidIdCardException e) {
            throw new RuntimeException(e);
        } catch (InvalidNumberPhoneException e) {
            throw new RuntimeException(e);
        } catch (InvalidEmailException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return people;
    }



    public static int addLecturerToDataBase(Person person) {
        var lecturer = (Lecturer)person;
        var ds = configDataSource();
        try(Connection conn = ds.getConnection()) {
            String sql = "INSERT INTO " +
                    "Instructor(IdCard,FullName,Address,BirthDate,Email," +
                    "NumberPhone,IdLecturer,Specialize,Salary,ExpYear)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?)";
            var prepar = conn.prepareStatement(sql);
            prepar.setString(1,lecturer.getIdCard());
            prepar.setString(2,lecturer.getFullName());
            prepar.setString(3,lecturer.getAddress());
            prepar.setDate(4,lecturer.getBirthDate());
            prepar.setString(5, lecturer.getEmail());
            prepar.setString(6, lecturer.getNumberPhone());
            prepar.setString(7, lecturer.getIdLecturer());
            prepar.setString(8, lecturer.getSpecialize());
            prepar.setDouble(9,lecturer.getSalary());
            prepar.setDouble(10, lecturer.getExpYear());
            return prepar.executeUpdate();
        } catch (SQLServerException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void showList(List<Person>people) {
        System.out.printf("%-30s%-15s%-25s%-15s%-28s%-15s%-15s%-14s\n",
                "Căn cước","Mã giảng viên",
                "Họ và tên","Ngày sinh", "Email",
                "Số điện thoại","Lương","Kinh nghiệm");
        for (var item1 :
                people) {
            var item = (Lecturer)item1;
            System.out.printf("%-30s%-15s%-25s%-15s%-28s%-15s%-15s%-14s\n",
                        item.getIdCard(),item.getIdLecturer(),
                    item.getFullName(),item.getBirthDate(),
                    item.getEmail(),item.getNumberPhone(),
                    item.getSalary(),item.getExpYear());
        }
    }

    public static void searchLecturerById(List<Person> people, Scanner input) {
        System.out.println("Nhập mã giảng viên:");
        String id = input.nextLine();
        System.out.printf("%-30s%-15s%-25s%-15s%-28s%-15s%-15s%-14s\n",
                "Căn cước","Mã giảng viên",
                "Họ và tên","Ngày sinh", "Email",
                "Số điện thoại","Lương","Kinh nghiệm");
        for(int i = 0; i < people.size(); i++) {
            var item = (Lecturer)people.get(i);
            if(id.compareTo(item.getIdLecturer()) == 0) {
                System.out.printf("%-30s%-15s%-25s%-15s%-28s%-15s%-15s%-14s\n",
                        item.getIdCard(),item.getIdLecturer(),
                        item.getFullName(),item.getBirthDate(),
                        item.getEmail(),item.getNumberPhone(),
                        item.getSalary(),item.getExpYear());
            }
        }
    }
    public static void searchLecturerBySalary(List<Person>people,
                                              Scanner input) {
        System.out.println("Nhập lương x: ");
        double x = Double.parseDouble(input.nextLine());
        System.out.printf("%-30s%-15s%-25s%-15s%-28s%-15s%-15s%-14s\n",
                "Căn cước","Mã giảng viên",
                "Họ và tên","Ngày sinh", "Email",
                "Số điện thoại","Lương","Kinh nghiệm");
        for(int i = 0; i < people.size(); i++) {
            var item = (Lecturer)people.get(i);
            if(item.getSalary() >= x) {
                System.out.printf("%-30s%-15s%-25s%-15s%-28s%-15s%-15s%-14s\n",
                        item.getIdCard(),item.getIdLecturer(),
                        item.getFullName(),item.getBirthDate(),
                        item.getEmail(),item.getNumberPhone(),
                        item.getSalary(),item.getExpYear());
            }
        }
    }
    public static void searchLecturerByName(List<Person> people,
                                            Scanner input) {
        System.out.println("Nhập tên cần tìm:");
        String name = input.nextLine();
        String regex = "^.*" + name + ".*$";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        System.out.printf("%-30s%-15s%-25s%-15s%-28s%-15s%-15s%-14s\n",
                "Căn cước","Mã giảng viên",
                "Họ và tên","Ngày sinh", "Email",
                "Số điện thoại","Lương","Kinh nghiệm");
        for (var item1 :
                people) {
            var item = (Lecturer)item1;
            Matcher matcher = pattern.matcher(item.getFullName());
            if(matcher.matches()) {
                System.out.printf("%-30s%-15s%-25s%-15s%-28s%-15s%-15s%-14s\n",
                        item.getIdCard(),item.getIdLecturer(),
                        item.getFullName(),item.getBirthDate(),
                        item.getEmail(),item.getNumberPhone(),
                        item.getSalary(),item.getExpYear());
            }
        }
    }
    public static void sortNameASC(List<Person> people) {
        Collections.sort(people,new SortNameASC());
        showList(people);
    }
    public static void sortSalaryDESC(List<Person> people) {
        Collections.sort(people,new SortSalaryDESC());
        showList(people);
    }
    public static void sortExpDESC(List<Person> people) {
        Collections.sort(people,new SortExpDESC());
        showList(people);
    }
    public static void setSalaryById(List<Person> people, Scanner input) {
        System.out.println("Nhập mã giảng viên:");
        String id = input.nextLine();
        var check = findOfLecturerById(people,id);
        if(check != null) {
            System.out.println("Nhập số lương:");
            double salary = Double.parseDouble(input.nextLine());
            check.setSalary(salary);
            updateToDataBase(id,salary);
            System.out.println("Cập nhật thành công.");
        } else {
            System.out.println("Cập nhật thất bại.");
        }
    }
    public static void removeLecturerById(List<Person> people, Scanner input) {
        System.out.println("Nhập mã giảng viên:");
        String id = input.nextLine();
        var check  = findOfLecturerById(people,id);
        if(check != null) {
            people.remove(check);
            updateToDataBase(id);
            System.out.println("Xóa thành công.");
        } else {
            System.out.println("Xóa thất bại.");
        }
    }

    private static void updateToDataBase(String id) {
        var ds = configDataSource();
        try(Connection connection = ds.getConnection()) {
            String sql = "DELETE FROM Instructor WHERE IdLecturer = ?";
            var prepareStm = connection.prepareStatement(sql);
            prepareStm.setString(1,id);
            prepareStm.executeUpdate();
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private static void updateToDataBase(String id, double salary) {
        var ds = configDataSource();
        try(Connection conn = ds.getConnection()) {
            String sql = "UPDATE Instructor SET  Salary = ? WHERE IdLecturer = ?";
            var prepar = conn.prepareStatement(sql);
            prepar.setDouble(1,salary);
            prepar.setString(2,id);
            prepar.executeUpdate();
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Lecturer findOfLecturerById(List<Person> people, String id) {
        for(int i = 0; i < people.size(); i++) {
            var item = (Lecturer)people.get(i);
            if(id.compareTo(item.getIdLecturer()) == 0) {
                return item;
            }
        }
        return null;
    }

    public static Person person(Scanner input) throws
            InvalidIdCardException, InvalidNumberPhoneException,
            InvalidEmailException, InputMismatchException, IllegalArgumentException {
            System.out.println("Căn cước:");
            String idCard = input.nextLine();
            System.out.println("Họ và tên:");
            String fullName = input.nextLine();
            System.out.println("Địa chỉ:");
            String address = input.nextLine();
            System.out.println("Sinh nhật:");
            String birthDate = input.nextLine();
            Date birth = Date.valueOf(birthDate);
            System.out.println("Email:");
            String email = input.nextLine();
            System.out.println("Số điện thoại:");
            String numberPhone = input.nextLine();
            Lecturer lecturer = new Lecturer();
            lecturer.setIdLecturer();
            System.out.println(lecturer.getIdLecturer());
            System.out.println("Chuyên môn:");
            String specialize = input.nextLine();
            System.out.println("Lương:");
            double salary = Double.parseDouble(input.nextLine());
            System.out.println("Năm kinh nghiệm:");
            double exp = Double.parseDouble(input.nextLine());
            return new Lecturer(idCard,fullName,address,birth,
                    email,numberPhone,lecturer.getIdLecturer(),specialize,
                    salary,exp);
    }


    private static SQLServerDataSource configDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser(USER);
        ds.setPassword(PASS_WORD);
        ds.setDatabaseName(DATABASE_NAME);
        ds.setPortNumber(PORT);
        ds.setServerName(SERVER_NAME);
        ds.setEncrypt(false);
        ds.setIntegratedSecurity(false);
        ds.setTrustServerCertificate(false);
        return ds;
    }
}
