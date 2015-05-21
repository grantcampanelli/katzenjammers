import java.sql.*;


public class Main {

    public static void main(String[] args) {
        //System.out.println("Hello World!");
        Connection conn = null;
        Statement stmt = null;
        String type, name, gender, phone, is_sole_proprieter, street, unit,
           city, region, zip_code, county, country;
        Date dob;
        Integer source_id;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Katzenjammers","grant","Soccer57");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Source";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("Querying each line from Source...");
            while(rs.next()) {
                type = rs.getString("type");
                name = rs.getString("name");
                gender = rs.getString("gender");
                dob = rs.getDate("dob");
                is_sole_proprieter = rs.getString("is_sole_proprieter");
                phone = rs.getString("phone");

                System.out.println("Type: "+type+" Name: "+name+" Gender: "
                        +gender+" DOB: "+dob+" SolePro: "+is_sole_proprieter);
            }
            System.out.print("Done reading Source");

            System.out.println("Querying each line from Address...");

            sql = "SELECT * FROM Address";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                source_id = rs.getInt("source_id");
                type = rs.getString("type");
                street = rs.getString("street");
                unit = rs.getString("unit");
                city = rs.getString("city");
                region = rs.getString("region");
                zip_code = rs.getString("zip_code");
                county = rs.getString("county");
                country = rs.getString("country");

                System.out.println("ID: "+source_id+" Type: "+type+" Street: "+street+" Unit: "+unit+
                        " City: "+city+" Region: "+region+" Zip: "+zip_code+
                        " County: "+county+" Country: "+country);
            }
            System.out.print("Done reading Addresses");

            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }
}
