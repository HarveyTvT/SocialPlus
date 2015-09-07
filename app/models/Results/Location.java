package models.Results;

/**
 * Created by harvey on 15-8-31.
 */
public class Location {
    private String id;//对应区号
    private int  result;//统计结果

    /****************************************************
     *                          setter and getter                                 *
     *                          of all attributes                                    *
     ****************************************************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
