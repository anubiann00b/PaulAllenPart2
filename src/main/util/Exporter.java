package main.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import main.util.data.Data;
import main.util.data.Packet;

/** Contains static methods to export Data[] */
public class Exporter {

    public static void exportTweetsOverTimeCsv(Data[] data, int hoursStep, String file, boolean time) throws IOException {
        int[] tweets = exportTweetsOverTime(data,hoursStep);

        StringBuilder result = new StringBuilder();

        Date current = (Date) data[0].time.clone();

        int milliStep = hoursStep*3600000;

        DateFormat df = new SimpleDateFormat("MM/dd HH:mm");

        if (time)
            result.append("Time,");
        result.append("Tweets\n");

        for (int i=0;i<tweets.length;i++) {
            if (time)
                result.append(df.format(current)).append(",");
            current.setTime(current.getTime()+milliStep);
            result.append(tweets[i]).append("\n");
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("exports\\" + file)));
        out.print(result.toString());
        out.close();
    }

    public static void exportTweetsOverTimeCsv(Data[] data, int hoursStep) throws IOException {
        exportTweetsOverTimeCsv(data,hoursStep,"tweets_over_time.csv",true);
    }

    public static String exportTweetsOverTimeAscii(Data[] data, int hoursStep, int scale) {
        int[] tweets = exportTweetsOverTime(data,hoursStep);

        StringBuilder result = new StringBuilder();

        Date current = (Date) data[0].time.clone();

        int milliStep = hoursStep*3600000;

        DateFormat df = new SimpleDateFormat("MM/dd HH:mm");

        for (int i=0;i<tweets.length;i++) {
            result.append(df.format(current)).append(" - ");
            current.setTime(current.getTime()+milliStep);
            result.append(df.format(current)).append(": ");
            for (int j=0;j<tweets[i]/scale;j++)
                result.append("-");
            result.append("\n");
        }
        return result.toString();
    }

    public static int[] exportTweetsOverTime(Data[] data, int hoursStep) {
        data = Sorter.sortByTime(data);

        Date current = (Date) data[0].time.clone();

        int milliStep = hoursStep*3600000;

        int pos = -1;

        long duration = data[data.length-1].time.getTime()-data[0].time.getTime();

        int[] results = new int[(int)(duration/milliStep)+1];

        for (Data item : data) {
            if (!item.time.before(current)) {
                current.setTime(current.getTime()+milliStep);
                pos++;
            }
            results[pos]++;
        }

        return results;
    }

    public static Data[][] exportRawTweetsOverTime(Data[] data, int hoursStep) {
        int[] split = exportTweetsOverTime(data, hoursStep);
        Data[][] set = new Data[split.length][1];
        int total = 0;
        Data[] temp;
        for(int i = 0; i < split.length; i++) {
            temp = new Data[split[i]];
            System.arraycopy(data, total, temp, 0, split[i]);
            set[i] = temp;
            total += split[i];
        }

        return set;
    }

    public static Packet[] exportToPackets(Data[] data) {
        Packet[] returnPacket = new Packet[data.length];

        for(int i = 0; i < data.length; i++)
            returnPacket[i] = Packet.getPacketFromData(data[i]);

        return returnPacket;
    }

    public static void exportAsJson(Data[][] data) {
        try {
            File jsonFile = new File("resources", "data.json");
            PrintWriter jsonWriter = new PrintWriter(new BufferedWriter(new FileWriter(jsonFile)));
            StringBuilder json = new StringBuilder();
            json.append("[");
            for(int i = 0; i < data.length; i++) {
                json.append("[\"").append(i + 1).append("\",[");
                for(int j = 0; j < data[i].length; j++) {
                    json.append(data[i][j].locationCoords.latitude);
                    json.append(",");
                    json.append(data[i][j].locationCoords.longitude);
                    json.append(",");
                    json.append(10);
                    if((j+1) < data[i].length)
                        json.append(",");
                }
                json.append("]]");
                if((i+1) < data.length)
                    json.append(",");
            }
            json.append("]");
            jsonWriter.print(json);
            System.out.println(json);
        } catch (IOException e) { System.err.println("IO Error: " + e); }
    }
    
    public static Data[][] normalize(Data[][] data, float scale) {
        Data[][] normal = new Data[][] {
            new Data[(int)(data[0].length * scale)],
            new Data[(int)(data[1].length * scale)],
            new Data[(int)(data[2].length * scale)],
            new Data[(int)(data[3].length * scale)],
            new Data[(int)(data[4].length * scale)],
            new Data[(int)(data[5].length * scale)],
            new Data[(int)(data[6].length * scale)],
            new Data[(int)(data[7].length * scale)],
            new Data[(int)(data[8].length * scale)],
            new Data[(int)(data[9].length * scale)]
        };
        for(int i = 0; i < data.length; i++)
            System.arraycopy(data[i], 0, normal[i], 0, normal[i].length);
        return normal;
    }
}
