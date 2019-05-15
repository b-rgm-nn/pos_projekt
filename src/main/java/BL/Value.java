
package BL;

import java.time.LocalDate;

/**
 * Represents a companies data on a single day
 * represents an entry in the db
 * @author Matthias
 */
public class Value implements Comparable<Value> {
    private LocalDate date;
    private double low;
    private double high;
    private double open;
    private double close;

    /**
     * pure data class
     * @param date date
     * @param low low
     * @param high high
     * @param open open
     * @param close  close
     */
    public Value(LocalDate date, double low, double high, double open, double close) {
        this.date = date;
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getOpen() {
        return open;
    }

    @Override
    public int compareTo(Value o) {
        return date.compareTo(o.date);
    }
}
