import model.*;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class Rule
{
    String description;
    Double[] weights;
    double threshold;

    public Rule(double nameWeight, double phoneWeight, double addressWeight,
                double specialtyWeight, double threshold) {
        weights = new Double[4];
        weights[0] = nameWeight;
        weights[1] = phoneWeight;
        weights[2] = addressWeight;
        weights[3] = specialtyWeight;

        this.threshold = threshold;
    }


    public boolean runRule(Score score) {

        // Run a rule given the provided attribute match scores contained
        //in the score

        return false;
    }
}