package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import main.util.Exporter;
import main.util.Parser;
import main.util.data.Data;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ParseException, IOException {
        Data[] data = Parser.parse("resources/filter_geotweets.csv");
        Data[][] timeData = Exporter.exportRawTweetsOverTime(data, 12);
        Exporter.exportAsJson(timeData);
    }
}
