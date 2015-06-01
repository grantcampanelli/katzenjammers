package model;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class Address
{
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
                   String region, String zip_code, String county, String country) {

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
