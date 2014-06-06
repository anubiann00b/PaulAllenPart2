package main.util.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import main.math.Vector2D;

public class Data {

    public Date time;

    public Point locationCoords;

    public Data(String line) throws Exception {
        String[] split = line.split(",");
        
        if(split.length == 3) {
            // Time
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
            time = df.parse(split[0].replace("\"", ""));

            //GPS-Lat and GPS-Long coordinates
            if (!split[1].replace("\"", "").isEmpty() || !split[2].replace("\"", "").isEmpty())
                locationCoords = new Point(Double.parseDouble(split[1]), Double.parseDouble(split[2]));
        } else
            throw new Exception(Arrays.toString(split));
    }

    public boolean isWithinDistance(Vector2D referenceVector, double distance) {
        if (locationCoords == null)
            return false;
        Vector2D locationVector = new Vector2D(locationCoords.longitude, locationCoords.latitude);
        double radius = 3956.6;
        return Vector2D.angle(locationVector, referenceVector) * radius <= distance;
    }
}
