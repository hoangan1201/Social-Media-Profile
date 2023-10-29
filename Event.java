
/**
 * Event Object
 */

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Event {
    String eventName;
    Date eventDate;


    public Event(String name0, String dateInString)  throws ParseException{
        eventName = name0;
        eventDate = convertStringToDate(dateInString);
    } //constructor

    public Date getDate(){
        return eventDate;
    }

    public String getName(){
        return eventName;
    }

    public Date convertStringToDate(String dateString)  throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = sdf.parse(dateString);
        return date;
    }

    public static void main(String args[]) throws ParseException{
        Event econFinal = new Event("econF", "22-05-2022 14:00:00");
        System.out.println(econFinal);
        System.out.println(econFinal.getDate());
        System.out.println();
        System.out.println();

    }
    

}