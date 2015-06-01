package model;

import java.sql.Date;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class Source
{
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
