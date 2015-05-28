import java.sql.*;


public class Main {

    public static class Source {
        public Integer id;
        public String type;
        public String name;
        public String gender;
        public Date dob;
        public String solProp;
        public String phone;
        public Integer primarySpecialty;
        public Integer secondarySpecialty;

        public Source(Integer id, String type, String name, String gender,
                      Date dob, String solProp, String phone, Integer primarySpecialty,
                      Integer secondarySpecialty) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.gender = gender;
            this.dob = dob;
            this.solProp = solProp;
            this.phone = phone;
            this.primarySpecialty =  primarySpecialty;
            this.secondarySpecialty = secondarySpecialty;
        }
    }

    public static class Specialties {
        public Integer parentId;
        public Integer specialtyId;
        public String title;
        public String code;
        public String website;

        public Specialties(Integer parentId, Integer specialtyId, String title,
                            String code, String website) {
            this.parentId = parentId;
            this.specialtyId = specialtyId;
            this.title = title;
            this.code = code;
            this.website = website;
        }
    }

    public static class Address {
        public Integer source_id;
        public String type;
        public String street;
        public String unit;
        public String city;
        public String region;
        public String zip_code;
        public String county;
        public String country;

        public Address(Integer source_id, String type, String street, String unit, String city,
                       String region, String zip_code, String county, String country;) {

            this.source_id =  source_id;
            this.type = type;
            this.street = street;
            this.unit = unit;
            this.city = city;
            this.region = region;
            this.zip_code = zip_code;
            this.county = county;
            this.country = country;
        }


    }

//    public void initArrayNull() {
//
//    }

    public static void main(String[] args) {
        //System.out.println("Hello World!");
        Connection conn = null;
        Statement stmt = null;
        String type, name, gender, phone, street, unit,
                city, region, zip_code, county, country, is_sole_proprieter, primary,
                title, code, website;
        Date dob;
        Integer source_id, parentId, specialtyId, primarySpecialty, secondarySpecialty;
        Integer numTuplesSpecialties = 0, numTuplesSource = 0, numTuplesAddress = 0, index = 0;


        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Katzenjammers","grant","Soccer57");
            stmt = conn.createStatement();
            String sql;
            System.out.println("Querying each line from Specialties...");
            sql = "SELECT * FROM Specialties";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                numTuplesSpecialties++;
            }
            System.out.print("Num Specialties: " + numTuplesSpecialties+"\n");

            Specialties[] specialties = new Specialties[numTuplesSpecialties];


            rs = stmt.executeQuery(sql);

            while(rs.next()) {

                // read in values to variables
                parentId = rs.getInt("parent_id");
                specialtyId = rs.getInt("specialty_id");
                title = rs.getString("title");
                code = rs.getString("code");
                website = rs.getString("website");

//                specialties[index].parentId = rs.getInt("parent_id");
//                specialties[index].specialtyId = rs.getInt("specialty_id");
//                specialties[index].title = rs.getString("title");
//                specialties[index].code = rs.getString("code");
//                specialties[index].website = rs.getString("website");

                // create new object in the array
                specialties[index] = new Specialties(parentId, specialtyId, title,
                        code, website);


                // print out specialties
                System.out.println("ParentId: "+specialties[index].parentId+" Id: "+
                        specialties[index].specialtyId+" Title: "+specialties[index].title+
                        " Code: "+specialties[index].code+" Website: "+
                        specialties[index].website);
                index++;
            }
            System.out.println("Done reading Specialties");


            System.out.println("Querying each line from Source...");
            sql = "SELECT * FROM Source";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                numTuplesSource++;
            }
            System.out.println("Num Sources: "+ numTuplesSource);

            Source[] source = new Source[numTuplesSource];

            rs = stmt.executeQuery(sql);
            index = 0;
            while(rs.next()) {
                source_id = rs.getInt("source_id");
                type = rs.getString("type");
                name = rs.getString("name");
                gender = rs.getString("gender");
                dob = rs.getDate("dob");
                is_sole_proprieter = rs.getString("is_sole_proprieter");
                phone = rs.getString("phone");
                primarySpecialty = rs.getInt("primary_specialty");
                secondarySpecialty  = rs.getInt("secondary_specialty");

                source[index] = new Source(source_id, type, name, gender, dob, is_sole_proprieter,
                        phone, primarySpecialty, secondarySpecialty);
                //System.out.println(primary);

                //System.out.println("SourceID: "+source_id+" Type: "+type+" Name: "+name+" Gender: "
                //        +gender+" DOB: "+dob+" SolePro: "+is_sole_proprieter);
            }
            System.out.println("Done reading Source");

            System.out.println("Querying each line from Address...");

            sql = "SELECT * FROM Address";
            rs = stmt.executeQuery(sql);

            while(rs.next()) {
                numTuplesAddress++;
            }
            System.out.println("Num Addresses: "+ numTuplesAddress);

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

                //System.out.println("ID: "+source_id+" Type: "+type+" Street: "+street+" Unit: "+unit+
                //        " City: "+city+" Region: "+region+" Zip: "+zip_code+
                //        " County: "+county+" Country: "+country);
            }
            System.out.print("Done reading Address");

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
