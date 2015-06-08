import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.*;

public class JDBCDeserialize
{

    public static List<Source> readInFromDatabase()
    {
        Connection conn = null;
        Statement stmt = null;
        String type, name, gender, phone, street, unit,
            city, region, zip_code, county, country, is_sole_proprieter, primary,
            title, code, website;
        Date dob;
        Integer source_id, parentId, specialtyId, primarySpecialty = null, secondarySpecialty = null;
        Integer numTuplesSpecialties = 0, numTuplesSource = 0, numTuplesAddress = 0, index = 0;
        Map<Integer, Source> sourceMap = new HashMap<Integer, Source>();


        try
        {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Katzenjammers", "grant", "Soccer57");
            stmt = conn.createStatement();
            String sql;
            ResultSet rs;

            // Set to query from Source
            sql = "SELECT * FROM Source LIMIT 1000";
            rs = stmt.executeQuery(sql);

            // Get number of tuples
            while (rs.next())
            {
                numTuplesSource++;
            }

            System.out.println("Num Sources: " + numTuplesSource);

            // initialize source array
            //Source[] source = new Source[numTuplesSource];
            List<Source> source= new ArrayList<Source>();

            // Start reading in Source
            rs = stmt.executeQuery(sql);
            index = 0;
            while (rs.next())
            {
                // read in source to variables
                source_id = rs.getInt("source_id");
                type = rs.getString("type");
                name = rs.getString("name");
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
                Source s = new Source(source_id, type, name, gender, dob,
                    is_sole_proprieter,
                    phone, primarySpecialty, secondarySpecialty);
                source.add(s);
                sourceMap.put(source_id, s);

                /*
                //Print out source array
                System.out.println("source_id: "+source[index].id+" type: "+source[index].type+" name: "+
                        source[index].name+" gender: "+source[index].gender+" dob: "+source[index].dob+
                        " solProprieter: "+source[index].solProp+" phone: "+source[index].phone+
                        " primarySpecialty: "+source[index].primarySpecialty+" secondarySpecialty: "+
                        source[index].secondarySpecialty);
                 */
            }

            System.out.println("Done reading Source");
            System.out.println("Querying each line from Address...");

            // set querty for Address
            sql = "SELECT * FROM Address";
            rs = stmt.executeQuery(sql);

            // get num tuples
            while (rs.next())
            {
                numTuplesAddress++;
            }
            System.out.println("Num Addresses: " + numTuplesAddress);

            // initialize addresses array
            Address[] addresses = new Address[numTuplesAddress];

            // read in addresses
            rs = stmt.executeQuery(sql);
            index = 0;
            while (rs.next())
            {
                // fix for getInt on null = 0
                if (rs.getString("source_id") == null)
                    source_id = null;
                else
                    source_id = rs.getInt("source_id");

                type = rs.getString("type");
                street = rs.getString("street");
                unit = rs.getString("unit");
                city = rs.getString("city");
                region = rs.getString("region");
                zip_code = rs.getString("zip_code");
                county = rs.getString("county");
                country = rs.getString("country");

                addresses[index] = new Address(source_id, type, street, unit, city, region, zip_code, county, country);
                if (sourceMap.get(source_id) != null) {
                    if (addresses[index].type.equals("MAIL"))
                    {
                        sourceMap.get(source_id).mailingAddress = addresses[index];
                    }
                    else if (addresses[index].type.equals("PRAC")) {
                        sourceMap.get(source_id).practiceAddress = addresses[index];
                    }
                }
                index++;
            }
            System.out.print("Done reading Address");

            System.out.println("Querying each line from Specialties...");
            sql = "SELECT * FROM Specialties";
            rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                numTuplesSpecialties++;
            }
            System.out.print("Num Specialties: " + numTuplesSpecialties + "\n");

            Specialties[] specialties = new Specialties[numTuplesSpecialties];


            rs = stmt.executeQuery(sql);
            index = 0;
            while (rs.next())
            {

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
        }
        catch (SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        catch (Exception e)
        {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally
        {
            //finally block used to close resources
            try
            {
                if (stmt != null)
                    stmt.close();
            }
            catch (SQLException se2)
            {
            }// nothing we can do
            try
            {
                if (conn != null)
                    conn.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }//end finally try
        }//end try

        // Arrays:
        //    specialties[]
        //    source[]
        //    addresses[]
        return null;
    }
}
