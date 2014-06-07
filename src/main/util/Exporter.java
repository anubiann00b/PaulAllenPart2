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
            jsonWriter.print("[");
            for(int i = 0; i < data.length; i++) {
                jsonWriter.print("[\"" + (i + 1) + "\",[");
                for(int j = 0; j < data[i].length; j++) {
                    jsonWriter.print(data[i][j].locationCoords.latitude);
                    jsonWriter.print(",");
                    jsonWriter.print(data[i][j].locationCoords.longitude);
                    jsonWriter.print(",");
                    jsonWriter.print(10);
                    if((j+1) < data[i].length)
                        jsonWriter.print(",");
                }
                jsonWriter.print("]]");
                if((i+1) < data.length)
                    jsonWriter.print(",");
            }
            jsonWriter.print("]");
            System.out.println("Finished.");
        } catch (IOException e) { System.err.println("IO Error: " + e); }
    }
    
    public static Data[][] normalize(Data[][] data, int size) {
        Data[][] normal = new Data[data.length][size];
        for(int i = 0; i < data.length; i++)
            System.arraycopy(data[i], 0, normal[i], 0, size);
        return normal;
    }
}
