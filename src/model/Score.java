package model;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class Score
{
    public Score() {};

    public Score(double nameScore, double phoneScore, double addressScore,
                 double specialtiesScore)
    {
        this.nameScore = nameScore;
        this.phoneScore = phoneScore;
        this.addressScore = addressScore;
        this.specialtiesScore = specialtiesScore;
    }

    public double nameScore, phoneScore, addressScore, specialtiesScore;
}
