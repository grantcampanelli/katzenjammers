package model;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class Specialties
{
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
