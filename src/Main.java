import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        //System.out.println("Hello World!");
        Connection conn = null;
        Statement stmt = null;
        String name = null;
        String gender = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Katzenjammers","grant","Soccer57");
            System.out.println("Hello World!");
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Source";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                name = rs.getString("name");
                gender = rs.getString("gender");
                System.out.println("Name: "+name+" Gender: "+gender);
            }
        } catch (Exception ex) {
            // handle the error
            System.out.println("Error");
        }
    }
}
