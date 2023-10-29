
/**
 * This is kind of a priority queue that stores Event object
 */

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


class ArrayEventQueue {
    Event[] A;
    int size;
    //public static final int CAPACITY = 100;

    public ArrayEventQueue(){
        A = new Event[100];
        size =0;
    } //construct

    public int size(){
        return size;
    }

    public boolean isEmpty() {
		return size == 0;
	}

    public void insert(Event event){
        A[size + 1] = event;
        size ++;
        Date dateOfEvent = event.getDate(); 
        /**
         * insert at the end of array
         * get the date of event that is inserted
         * if there are more than 1 object in queue
         * => loop the array from end to start
         * to compare the inserted event with events before it
         * if it happens before => swap
         */

        if (size > 1){
            for (int i=size; i > 1; i--){
                if (dateOfEvent.before(A[i - 1].getDate())){
                    swap(i, i -1);
                }
            }
        }
    }

    //method to delete the first event in queue
    public void deleteFirstEventInQ(){
        if (isEmpty()){

        } else {
            for (int i=1; i< size; i++){
                swap(i, i+1);
            }
            size--;
        }
        /**
         * loop from 1 to end to swap 
         * the first object until it reaches the end of the array
         * then decrement size
         */
    }

    //this method update queue to remove event that has already happened
    public void updateQueue(){
        Date dateToCheck;
        Date now = new Date(); //get current time
        /**
         * loop through the queue
         * if its date is before current time
         * => delete
         */
        for (int i=1; i <= size; i++){
            dateToCheck = A[i].getDate();

            if (now.after(dateToCheck)){
                deleteFirstEventInQ();
            }
        }
    }

    private void swap(int firstIndex, int secondIndex) {
		Event temp = A[firstIndex];
		A[firstIndex] = A[secondIndex];
		A[secondIndex] = temp;
	}

    //This method returns all the events in the queue in String
    public String printOutQueue(){
        String stringToReturn;
        stringToReturn = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");  
        //format to convert date object of event into String

        for (int i=1; i <= size; i++){
            if (i == size){
                stringToReturn = stringToReturn + sdf.format(A[i].getDate()) + ": " + A[i].getName();
            } else{
                stringToReturn = stringToReturn + sdf.format(A[i].getDate()) + ": " + A[i].getName() + "; ";
            }
        }

        return stringToReturn;
    }

    //main method to test
    public static void main(String[] args) throws ParseException{
        ArrayEventQueue eventList = new ArrayEventQueue();
        System.out.println(eventList.isEmpty());
        System.out.println(eventList.size());
        String name = "Math Final";
        String dateee = "16-05-2022 13:00:00";
        Event mathFinal = new Event(name,dateee);
        Event csFinal = new Event("CS Final", "15-05-2022 14:00:00");

        eventList.insert(mathFinal);
        eventList.insert(csFinal);
        System.out.println(eventList.size());
        System.out.println(eventList);
        System.out.println(eventList.printOutQueue());
    }
}