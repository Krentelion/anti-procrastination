package com.ap;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;

public class Main 
{
    // for now redundant as program is assuming the .ics file is in working directory
    public String fileLocation = inputOutput.acquireFile();

    public static void main(String[] args) throws Exception
    {
        // acquire ics file from root directory
        FileInputStream fin = new FileInputStream("feed2.ics");
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(fin);

        // put calendar object into tempAL2 object arraylist and declare new convertedCalendar string arraylist
        List tempAL2 = Arrays.asList(calendar.split());
        ArrayList<String> convertedCalendar = new ArrayList<String>();

        // for loop to add objects from tempAL2 into strings for convertedCalendar
        Object fakyu = new Object();
        for(int i = 0; i < tempAL2.size(); i++){
            fakyu = tempAL2.get(i);
            convertedCalendar.add(String.valueOf(fakyu));
        }

        // declaring new arraylists for.. obvious reasons. stupid mf
        ArrayList<String> assignmentName = new ArrayList<>();
        ArrayList<String> dtstamps = new ArrayList<>(); // Always will be same length --- 8dig + T + 6dig + Z
        ArrayList<Long> dtstampsCleaned = new ArrayList<>();

        // finding each instance of "SUMMARY:" & "DTEND:" to add to respective arraylists
        for (String event : convertedCalendar) {
            if (event == null) continue;
            for (String line : event.split("\\r?\\n")) {
                if (line.startsWith("SUMMARY:")) {
                    int start = "SUMMARY:".length();
                    assignmentName.add(line.substring(start).trim());
                    break; // stop after first DTEND in this event
                }
            }

            for (String line : event.split("\\r?\\n")) {
                if (line.startsWith("DTEND:")) {
                    int start = "DTEND:".length();
                    dtstamps.add(line.substring(start).trim());
                    break; // stop after first DTEND in this event
                }
            }
        }

        // seeing if u somehow fucked up the icalendar
        if(assignmentName.size() != dtstamps.size()) {
            System.err.println("ur shit aint right brodie get a proper fkin icalendar and come back 2 me retard");
        }

        // removing T and Z from the DTEND arraylist elements
        long tempLong;
        String tempString;
        for(String line : dtstamps) {
            if (line == null) continue;
            tempString = line.replace("T", "").replace("Z", "");
            tempLong = Long.parseLong(tempString);
            dtstampsCleaned.add(tempLong);
        }

        // Making a new map & turning it into a TreeMap that should sort itself
        Map<Long, String> unsortedMap = new HashMap();
        for(int i = 0; i < dtstamps.size(); i++) {
            // fakyu2 = dtstamps.get(i);
            // System.out.println(fakyu2);
            // fakyu2 = assignmentName.get(i);
            // System.out.println(fakyu2);

            unsortedMap.put(dtstampsCleaned.get(i), assignmentName.get(i));
        }

        // should be fully sorted in order of lowest date to highest date
        Map<Long, String> sortedMap = new TreeMap<>(unsortedMap);

        // debug to print out all elements of the treemap
        for (Map.Entry<Long, String> entry : sortedMap.entrySet()){ 
            System.out.println(entry.getKey() + " => " + entry.getValue()); 
        }
    }
}
