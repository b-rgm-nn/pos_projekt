package BL;


import java.time.LocalDate;
import java.util.Comparator;

/**
 * Local Date Comparator
 * useless, but can't be bothered to fix the code that uses it and retest everything
 * @author Matthias
 */
public class LocalDateComparator implements Comparator<LocalDate>{

    @Override
    public int compare(LocalDate o1, LocalDate o2) {
        return o1.compareTo(o2);
    }
    
}
