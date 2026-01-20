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
import net.fortuna.ical4j.model.component.VEvent;

public class Maintest{

    //replaces TreeMap
    static class Assignment{
        String name;  //"Class | Assignment"
        ZonedDateTime due; //exact due date
    }
    /*
      load ics file. ical4j parses and builds Calendar object
    */
    FileInputStream fin = new FileInputStream("feed2.ics");
    CalendarBuilder builder = new CalendarBuilder();
    Calendar calendar = builder.build(fin);


   /*
    Extract VEvent obj. shows summary, location, dtend, etc.
   */

    List<VEvent> assignmentEvents = new ArrayList<>();

    for (Component c : calendar.getComponents(Component.VEVENT)){
        assignmentEvents.add((VEvent) c);
    }

    /*
    Convert VEvents to assignment obj. replaces string parsing.
    */

    List<Assignment> assignments = new ArrayList<>();

    for (VEvent event : assignmentEvents) {

        Assignment assignment = new Assignment();

        //class name
        /*String location = event.getLocation() != null
                ? event.getLocation().getValue()
                : "Unknown Class";*/ 
        String location;
        if (event.getLocation() != null) {
            location = event.getLocation().getValue();
        } else {
            location = "Unknown Class";
        } 
        //assignment title
        String summary;
        if (summary.getSummary() != null) {
            location = event.getSummary().getValue();
        } else {
            location = "Unknown Assignment";
        } 
        assignment.name = location + " | " + summary;

        //DTEND -> due date. already parsed into Date but convert to ZonedDateTime just in case
        EndDate endDate = event.getEndDate();
        Date date = endDate.getDate();
        Instant instant = date.toInstant();
        assignment.due = instant.atZone(ZoneId.systemDefault());

        assingments.add(assignment);
    }
}


/*public class Maintest
{
    // for now redundant as program is assuming the .ics file is in working directory
    //public String fileLocation = inputOutput.acquireFile();

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
        String assignmentClass = new String();
        for (String event : convertedCalendar) {
            if (event == null) continue;

            // grabbing "location," which should be the class name (or location) and puts it into the assignmentClass string
            for (String line : event.split("\\r?\\n")) {
                if (line.startsWith("LOCATION:")) {
                    int start = "LOCATION:".length();
                    assignmentClass = line.substring(start).trim();
                    break; // stop after first LOCATION in this event
                }
            }

            // grabbing "summary," which should be what assignment it is and appends it onto the assignmentName arraylist as "Class | Assignment"
            for (String line : event.split("\\r?\\n")) {
                if (line.startsWith("SUMMARY:")) {
                    int start = "SUMMARY:".length();
                    assignmentClass = assignmentClass + " | " + line.substring(start).trim();
                    assignmentName.add(assignmentClass);
                    break; // stop after first SUMMARY in this event
                }
            }

            // grabbing "DTEND," which should be when the assignment is due (?) and uses it as the key for the treemap later on
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
}*/
