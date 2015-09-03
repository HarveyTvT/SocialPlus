package models;
import java.util.Date;

/**
 * Created by harvey on 15-8-31.
 */
public class Time {

    private Date time;
    private double result;//统计结果

    /****************************************************
     *                          setter and getter                                 *
     *                          of all attributes                                    *
     ****************************************************/

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
