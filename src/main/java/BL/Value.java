
package BL;

import java.time.LocalDate;

public class Value {
    private LocalDate date;
    private double low;
    private double high;
    private double open;
    private double close;

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

    /**
     * blaze it 420
     * @return high
     */
    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getOpen() {
        return open;
    }
}
