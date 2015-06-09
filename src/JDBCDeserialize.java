import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import model.*;



public class JDBCDeserialize {
//    public static void main(String[] args) {
//        readInFromDatabase();
//    }

    // !!! SET NUM SOURCES HERE! !!!




    public static Integer LimitSources = 0;
    public static Integer NumSources = 500;

    public static void ReadNumSources() {
        Scanner in = new Scanner(System.in);

        System.out.println("How many sources would you like to limit to? Use 0 to set no limit");
        NumSources = in.nextInt();
        System.out.println("You chose "+ NumSources);

        if(NumSources == 0)
            LimitSources = 0;
        else
            LimitSources = 1;

    }

    public static String getOutputDirectory() {
        if(LimitSources == 1) {
            return  "TestOutput/" + Integer.toString(NumSources) + "_";
        }
        else {
            return "TestOutput/Full_Test_";
        }
    }

    public static List<Source> readInFromDatabase() {
        Connection conn = null;
        Statement stmt = null;
        String type, name, gender, phone, street, unit,
                city, region, zip_code, county, country, is_sole_proprieter, primary,
                title, code, website, prefix, suffix, medCredential;
        Date dob;
        Integer source_id, parentId, specialtyId, primarySpecialty = null, secondarySpecialty = null;
        Integer numTuplesSpecialties = 0, numTuplesSource = 0, numTuplesAddress = 0, index = 0;
        Map<Integer, Source> sourceMap = new HashMap<Integer, Source>();


        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Katzenjammers", "cpe366", "Soccer57");
            stmt = conn.createStatement();
            String sql;
            ResultSet rs;

            // Set to query from Source
            if(LimitSources == 1)  {
                sql = "SELECT * FROM Source LIMIT "+Integer.toString(NumSources);
            }
            else {
                sql = "SELECT * FROM Source";
            }



            rs = stmt.executeQuery(sql);

            // Get number of tuples
            while (rs.next()) {
                numTuplesSource++;
            }

            System.out.println("Num Sources: " + numTuplesSource);

            // initialize source array
            //Source[] source = new Source[numTuplesSource];
            List<Source> source = new ArrayList<Source>();

            // Start reading in Source
            rs = stmt.executeQuery(sql);
            index = 0;
            while (rs.next()) {
                // read in source to variables
                source_id = rs.getInt("source_id");
                type = rs.getString("type");
                prefix = rs.getString("name_prefix");
                name = rs.getString("name");
                suffix = rs.getString("name_suffix");
                medCredential = rs.getString("medical_credential");
                gender = rs.getString("gender");
                dob = rs.getDate("dob");
                is_sole_proprieter = rs.getString("is_sole_proprieter");
                phone = rs.getString("phone");

                // fix for getInt on null = 0
                if (rs.getString("primary_specialty") == null)
                    primarySpecialty = null;
                else
                    primarySpecialty = rs.getInt("primary_specialty");

                // fix for getInt on null = 0
                if (rs.getString("secondary_specialty") == null)
                    secondarySpecialty = null;
                else
                    secondarySpecialty = rs.getInt("secondary_specialty");

                // create new source object and add to array
                //source[index]
                Source s = new Source(source_id, type, prefix, name,suffix, medCredential,
                        gender, dob, is_sole_proprieter,
                        phone, primarySpecialty, secondarySpecialty);
                source.add(s);
                sourceMap.put(source_id, s);


                //Print out source array
//                System.out.println("source_id: "+s.id+" type: "+s.type+" name: "+
//                        s.name+" gender: "+s.gender+" dob: "+s.dob+
//                        " solProprieter: "+s.solProp+" phone: "+s.phone+
//                        " primarySpecialty: "+s.primarySpecialty+" secondarySpecialty: "+
//                        s.secondarySpecialty);

            }

            System.out.println("Done reading Source");
            System.out.println("Querying each line from Address...");

            // set querty for Address
            sql = "SELECT * FROM Address";
            rs = stmt.executeQuery(sql);

            // get num tuples
            while (rs.next()) {
                numTuplesAddress++;
            }
            System.out.println("Num Addresses: " + numTuplesAddress);

            // initialize addresses array
            Address[] addresses = new Address[numTuplesAddress];

            // read in addresses
            rs = stmt.executeQuery(sql);
            index = 0;
            while (rs.next()) {
                // fix for getInt on null = 0
                if (rs.getString("source_id") == null)
                    source_id = null;
                else
                    source_id = rs.getInt("source_id");

                type = rs.getString("type") == null ? "NULL" : rs.getString("type");
                street = rs.getString("street") == null ? "NULL" : rs.getString
                        ("street");
                unit = rs.getString("unit") == null ? "NULL" : rs.getString("unit");
//                if (rs.getString("unit") == null) {
//                    System.out.println("found a null literal");
//                    System.exit(1);
//                }
                city = rs.getString("city") == null ? "NULL" : rs.getString("city");
                region = rs.getString("region") == null ? "NULL" : rs.getString
                        ("region");
                zip_code = rs.getString("zip_code") == null ? "NULL" : rs.getString
                        ("zip_code");
                county = rs.getString("county") == null ? "NULL" : rs.getString
                        ("county");
                country = rs.getString("country") == null ? "NULL" : rs.getString
                        ("country");

                addresses[index] = new Address(source_id, type, street, unit, city, region, zip_code, county, country);
                if (sourceMap.get(source_id) != null) {
                    if (addresses[index].type.equals("MAIL")) {
                        sourceMap.get(source_id).mailingAddress = addresses[index];
                    } else if (addresses[index].type.equals("PRAC")) {
                        sourceMap.get(source_id).practiceAddress = addresses[index];
                    }
                }
                index++;
            }
            System.out.println("Done reading Address");

            System.out.println("Send addresses to make file");

            printAddressesToFile(addresses, numTuplesAddress);

            System.out.println("Made it back from making addresses file");

            System.out.println("Querying each line from Specialties...");
            sql = "SELECT * FROM Specialties";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                numTuplesSpecialties++;
            }
            System.out.print("Num Specialties: " + numTuplesSpecialties + "\n");

            Specialties[] specialties = new Specialties[numTuplesSpecialties];


            rs = stmt.executeQuery(sql);
            index = 0;
            while (rs.next()) {

                // read in values to variables
                // reading in null with getInt makes it 0, which is not good
                if (rs.getString("parent_id") == null)
                    parentId = null;
                else
                    parentId = rs.getInt("parent_id");

                // fix for getInt on null = 0
                if (rs.getString("specialty_id") == null)
                    specialtyId = null;
                else
                    specialtyId = rs.getInt("specialty_id");

                title = rs.getString("title");
                code = rs.getString("code");
                website = rs.getString("website");

                // create new object in the array
                specialties[index] = new Specialties(parentId, specialtyId, title,
                        code, website);


                /*
                // print out specialties
                System.out.println("ParentId: "+specialties[index].parentId+" Id: "+
                        specialties[index].specialtyId+" Title: "+specialties[index].title+
                        " Code: "+specialties[index].code+" Website: "+
                        specialties[index].website);
                */
                index++;
            }

            System.out.println("Done reading Specialties");
            System.out.println("Querying each line from Source...");

            rs.close();
            stmt.close();
            conn.close();
            return source;
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

        return null;
    }

    public static void printAddressesToFile(Address[] addresses, Integer numTuplesAddress) {

        File AddressFile = new File(getOutputDirectory() + "Addresses_Katzenjammers.txt");
        FileWriter addressWriter;
        try {
            addressWriter = new FileWriter(AddressFile, false);
            PrintWriter pw = new PrintWriter(addressWriter);

            pw.print("Source Identifier\t");
            pw.print("Address Type\t");
            pw.print("Street\t");
            pw.print("Unit\t");
            pw.print("City\t");
            pw.print("Region\t");
            pw.print("Post Code\t");
            pw.print("County\t");
            pw.print("Country\n");

            Address a;
            for (int i = 0; i < numTuplesAddress; i++) {
                a = addresses[i];
                printAddressIntegerToFile(pw, a.source_id);
                printAddressStringToFile(pw, a.type, "\t");
                printAddressStringToFile(pw, a.street, "\t");
                printAddressStringToFile(pw, a.unit, "\t");
                printAddressStringToFile(pw, a.city, "\t");
                printAddressStringToFile(pw, a.region, "\t");
                printAddressStringToFile(pw, a.zip_code, "\t");
                printAddressStringToFile(pw, a.county, "\t");
                printAddressStringToFile(pw, a.country, "\n");
            }

            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printAddressIntegerToFile(PrintWriter pw, Integer item) {
        if (item == null)
            pw.print("NULL\t");
        else
            pw.print(item + "\t");
    }

    public static void printAddressStringToFile(PrintWriter pw, String item, String delimiter) {
        if (item == null) {
            pw.print("NULL" + delimiter);
        } else {
            pw.print(item + delimiter);
        }
    }

}
