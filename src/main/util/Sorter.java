package main.util;

import java.util.Arrays;
import java.util.Comparator;
import main.util.data.Data;

/** Contains static methods to sort Data[] */
public class Sorter {
    
    /* Sorts data by time. */
    public static Data[] sortByTime(Data[] data) {
        Arrays.sort(data,new TimeComparator());
        return data;
    }
}

class TimeComparator implements Comparator<Data> {

    @Override
    public int compare(Data o1, Data o2) {
        if (o1.time.after(o2.time))
            return 1;
        else if (o1.time.before(o2.time))
            return -1;
        return 0;
    }
}