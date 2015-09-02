package models;

/**
 * Created by harvey on 15-8-31.
 */
public class Time {

    private int time;//对应时刻
    private double result;//统计结果

    /****************************************************
     *                          setter and getter                                 *
     *                          of all attributes                                    *
     ****************************************************/

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
