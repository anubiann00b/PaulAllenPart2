package main.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.util.data.Data;

public class Parser {

    /** Reads data from a text file. */
    public static Data[] parse(String ref) throws FileNotFoundException, ParseException, IOException {
        return parse(ref, 247256);
    }
    
    public static Data[] parse(String ref, int subset) throws FileNotFoundException, ParseException, IOException {
        BufferedReader reader;
        Data[] data = new Data[subset];

        reader = new BufferedReader(new FileReader(ref));

        reader.readLine(); // Header.
        String line;
        for (int i=0;i<subset;i++) {
            line = reader.readLine();
            try {
                data[i] = new Data(line);
            } catch(Exception ex) {}
        }
        return clean(data);
    }

    public static Data[] clean(Data[] data) {
        List<Data> da = new ArrayList(Arrays.asList(data));
        for(int i = 0; i < da.size(); i++) {
            if(da.get(i) == null) {
                da.remove(i);
                i--;
            }
        }
        return da.toArray(new Data[da.size()]);
    }
}
