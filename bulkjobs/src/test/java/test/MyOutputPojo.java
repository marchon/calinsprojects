package test;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Calin
 * Date: 02.08.2012
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public class MyOutputPojo implements Serializable {
    private int result;
    private String resultString;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    @Override
    public String toString() {
        return "MyOutputPojo{" +
                "result=" + result +
                ", resultString='" + resultString + '\'' +
                '}';
    }
}
