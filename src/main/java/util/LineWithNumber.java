/*
 * Author Stuart Davies (802996013)
 * Local Test Server development 15/09/2017
 * BT eCommerce 
 */
package util;

/**
 * A line of text including it's line number
 *
 * @author - Stuart Davies
 */
public class LineWithNumber {

    private int number;
    private String text;

    public LineWithNumber(int number, String text) {
        this.number = number;
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

}
