/**
 * Final Project COM 212 - Spring 2022
 * Instructor: Prof. William Tarimo
 *  
 * Filename: MySocialProfile.java 
 * Supporting files, classes:
 * - Event.java: This class creates Event object with event name and date 
 * to be used for list of upcoming events of user.
 * 
 * - ArrayEventQueue.java: This is an array priority queue of Event objects. Event 
 * that comes earliest will be stored in the front of the array
 * 
 * - MySocialProfile.txt: This is a text file used to store the profile created by the user
 * so it can be loaded the next time he opens the program
 * 
 * Descriptopn:
 * This program is a console-based social networking profile that focuses on a single user's 
 * account details, upcoming events, friends, and a timeline of posts. It will run in the 
 * terminal or command prompt and will store the user's profile data in a single text file.
 * 
 *@author: An (atran5@conncoll.edu)
 *@author: ChristopherTowey (Ctowey@conncoll.edu)
 * 
 */

import java.io.*;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class MySocialProfile {
    
    private String fullName;
    private String emailAddress;
    private String password;
    private String classYear;

    ArrayEventQueue eventLists;
    //priority queue to store upcoming events

    String[] timelinePosts = new String[100];
    int timelineSize = 0;
    //array to store timeline posts

    String[] friendsList = new String[500];
    int friendsSize = 0;
    //array to store friends

    //declare MySocialProfile object
    public MySocialProfile(String name0, String email0, String password0, String classYear0){
        fullName = name0;
        emailAddress = email0;
        password = password0;
        classYear = classYear0;
        eventLists = new ArrayEventQueue();
    }

    //method to add new post to timeline
    public void addTimelinePosts(String newPost){
        this.timelinePosts[timelineSize] = newPost;
        timelineSize = timelineSize + 1;
        //add new post to array and increment size
    }

    //method to return all timeline posts to display on profile
    public String showTimelinePosts(){
        String stringToReturn;
        stringToReturn = "\n";
        
        if (timelineSize == 0){
            //if array is empty => no posts
            stringToReturn = stringToReturn + "No posts.";
        } else{
            //run loop from the last component in array to the first so newest posts will be displayed first
            for (int i = timelineSize - 1; i > -1; i--){
                
                if (i != timelineSize-1){ 
                    stringToReturn =   stringToReturn + ", " + '"' + timelinePosts[i] + '"' ;
                } else { //if it's the first post to be displayed => no need comma
                    stringToReturn = stringToReturn + '"' + timelinePosts[i] + '"';
                }    
            }   
        }
        return stringToReturn; //return 
    }


    //4 methods to return the 4 basic information
    public String getName(){
        return fullName;
    }

    public String getEmail(){
        return emailAddress;
    }

    public String getPassword(){
        return password;
    }

    public String getClassYear(){
        return classYear;
    }

    //method to add new friend
    public void addFriend(String friendEmailAddress){
        this.friendsList[friendsSize] = friendEmailAddress;
        friendsSize = friendsSize + 1;
        //add to array of friends and increment size
    }

    //method to remove friend
    public void removeFriend(String friendToDelete){
        boolean exist;
        exist = false;
        //boolean to check if the friend user inputed is in his friend list

        if (friendsSize == 0){ //when friend list is alreay empty
            System.out.println("List of friends is empty");
        } else{
            for (int i=0; i < friendsSize; i++){
                //loop through the array
                if (friendsList[i].equals(friendToDelete)){ //when find matched name
                    for (int j=i; j < friendsSize; j++){
                        friendsList[j] = friendsList[j+1]; 
                        //move all the other friends after that back by 1 index to delete the friend matched                       
                    }
                    friendsSize = friendsSize - 1;  //decrement size
                    exist = true; 
                }
            } 
            //System.out.println(friendsList);
            
            if (exist == true){                     
            } else {
                System.out.println("That friend is not on your friend list.");
                //if find no match => the friend inputted is not on friend list
            }            
        }
    }

    //method to return all friends in friend list
    public String showFriendsList(){
        String stringToReturn;
        stringToReturn = "\n";

        if (friendsSize == 0){ //when the array is empty
            stringToReturn = stringToReturn + "No friends.";

        } else {

            for (int i=0; i < friendsSize; i++){ 
                //loop through the array
                stringToReturn = stringToReturn + '"' + friendsList[i] + '"';

                if (i != friendsSize - 1){
                    //if it's not the last friend in the array => add coma to the string returned
                    stringToReturn = stringToReturn + ", ";
                } 
            }
        }
        return stringToReturn;
    }

    //method to add event to the event list
    public void addEvent(String name, String date) throws ParseException{
        Event event = new Event(name,date); //create Event object to be added to list

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss"); //create date format
        Date now = new Date(); //get current time
        Date dateInserted = sdf.parse(date); //create date object from the date inserted to compare with current time

        if (dateInserted.after(now)){
            eventLists.insert(event);
            //if the event happens in the future => add
        } else {
            //if the event has already happened => notify user
            System.out.println("The event you entered has already passed. Please try again and enter an event in the future!");
        }
    }

    //this method updates event lists to remove events that already happened
    //this method is used when loading existing profile and there may be 
    //events that has passed
    public void updateEventList(){
        eventLists.updateQueue();
        //update queue method in ArrayEventQueue class
    }

    //This method writes all the details or save any changes of the profile to the text file
    public void writeToTextFile(String content, String fileName){
        try{
            //open file, write and then close file
            FileWriter fw = new FileWriter(fileName);
            fw.write(content);
            fw.close();

        } catch(IOException ex){
            System.out.println("Can not write to file");
            System.exit(0);
        }
    }
}


/**
 *Main class to run the program 
 */

class Main{

    //This method load existing profile from the text file
    public static MySocialProfile loadExistProfile ()throws ParseException, IOException{
        
        try {
            MySocialProfile profileFromTxtFile; //declare MySocialProfile object

            FileInputStream file = new FileInputStream("MySocialProfile.txt");

            Scanner textFile = new Scanner(file);
            while (textFile.hasNextLine()){ //as long as the text has the next line

                //first 4 lines will be the 4 basic infos
                String name = textFile.nextLine();
                String email = textFile.nextLine();
                String password = textFile.nextLine();
                String classYear = textFile.nextLine();

                //use the basic infos get from the text to create MySocialProfile object
                profileFromTxtFile = new MySocialProfile(name, email, password, classYear);

                //read events 
                //Then read events which is the fifth line
                String events = textFile.nextLine();

                String eventName;
                String eventTime;
                if (events.equals("No upcoming event.")){
                    //if there is no events then no action needed
                } else{
                    Scanner eventsScanner = new Scanner(events);
                    //this scanner runs through the fifth line
                    eventsScanner.useDelimiter(";");
                    //The ';' is the separation of each event

                    String e;
                    while(eventsScanner.hasNext()){

                        e = eventsScanner.next();

                        Scanner eScanner = new Scanner(e);
                        //create another scanner that scan through each event individually

                        eScanner.useDelimiter(": ");
                        //The ": " separates each event of its name and its date

                        eventTime = eScanner.next();
                        eventName = eScanner.next();

                        profileFromTxtFile.addEvent(eventName, eventTime);
                        //Then add event to the MySocialProfile object with info from text file
                    }
                }

                //read posts
                //below is quite similar but for timeline posts
                String posts = textFile.nextLine();
                if (posts.equals("No posts.")){

                } else{
                    Scanner postScanner = new Scanner(posts);
                    String[] postsFromTxtFile = new String[100];
                    int postArrIndex =0;
                    //create array to store timeline posts from text file
                    String postContent;
                    postScanner.useDelimiter(", ");

                    while(postScanner.hasNextLine()){
                        postContent = postScanner.next();

                        postContent = postContent.substring(1, postContent.length()-1);
                        //use substring to remove quotation mark 

                        postsFromTxtFile[postArrIndex] = postContent;
                        //then add to array and increase index
                        postArrIndex++;
                    }

                    /*loop through the array to add posts from array to profile
                    we have to loop from back to front because the earlier posts
                    stand at the end of the array */
                    for (int j=postArrIndex-1; j > -1; j--){
                        profileFromTxtFile.addTimelinePosts(postsFromTxtFile[j]);
                    }
                }

                //read friends
                //Similar but for friends
                //here we dont need array because friend list does not have to be in order
                String friends = textFile.nextLine();
                if (friends.equals("No friends.")){

                } else{
                    Scanner friendsScanner = new Scanner(friends);

                    String friendName;
                    friendsScanner.useDelimiter(", ");
                    
                    while (friendsScanner.hasNextLine()){
                        friendName = friendsScanner.next();
                        friendName = friendName.substring(1, friendName.length()-1);
                        //use substring to remove quotation mark

                        profileFromTxtFile.addFriend(friendName);
                    }
                }
                file.close(); //close file
                return profileFromTxtFile; //return MySocialProfile object
            }
        } catch(FileNotFoundException ex){
            System.out.println("File not Found");
            System.exit(0);
        }
        return null;
    }

    //this method return all the details of the profile to be printed out on display
    public static String displayProfileInfo(MySocialProfile profile){
        String stringToReturn;

        //get the basic infos from the object and add to the returned string
        String profileName = profile.getName();
        String profileEmail = profile.getEmail();
        String profilePassword = profile.getPassword();
        String profileClassYear = profile.getClassYear();
        stringToReturn = profileName + "\n" + profileEmail + "\n" + profilePassword + "\n" + profileClassYear + "\n";

        /*if there is no upcoming events, notify user
        else, use printOutQueue() method from ArrayEventQueue class
        that returns all events in the queue with specific name and date*/
        if (profile.eventLists.isEmpty()){
            stringToReturn = stringToReturn + "No upcoming event.";
        } else {
            stringToReturn = stringToReturn + profile.eventLists.printOutQueue();
        }
        //then timeline posts and friends
        stringToReturn = stringToReturn + profile.showTimelinePosts();

        stringToReturn = stringToReturn + profile.showFriendsList();

        return stringToReturn;
    }

    //Main Program
    public static void main(String[] args) throws ParseException, IOException{
        int userChoice;

        Scanner scan = new Scanner(System.in);

        do
        {
            MySocialProfile userProfile;
            System.out.println("\nEnter a letter to choose your option:");
            System.out.println();
            
            System.out.print(" Create a new account (r) \n Load existing profile (l) \n Quit program (q) \n\t\t ----> ");
            userChoice = getChar();

            switch(userChoice)
            {
                case 'r':

                /**
                 * Prompt user for basic information to create a profile
                 */
                    System.out.print("Please enter your email: ");
                    String inputEmail = scan.nextLine();

                    System.out.print("\nPlease enter your name: ");
                    String inputName = scan.nextLine();

                    System.out.print("\nCreate your password: ");
                    String inputPassword = scan.nextLine();

                    System.out.print("\nPlease enter your class year: ");
                    String inputClassYear = scan.nextLine();

                    userProfile = new MySocialProfile(inputName, inputEmail, inputPassword, inputClassYear);
                    //Create a new MySocialProfile object with the information input

                    System.out.print("\nYour profile has been created. Your username is " + inputName);
                    //Notify of successful registration

                    userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                    //write profile to text file

                    System.out.print("\n" + displayProfileInfo(userProfile));
                    //display user's profile

                    do
                    {
                        System.out.print("\n\nSelect your option: \nPost to timeline (p) \nAdd event (a) \nView friend list (v)");
                        System.out.print("\nAdd friend (f) \nDelete friend (d) \nLog out (e) \n\t ---->");
                        userChoice = getChar();
                        switch(userChoice)
                        {
                            case 'a': //Add Event
                                //ask for event name
                                System.out.print("Event name: ");
                                String eventName = scan.nextLine(); 
                                //ask for event date
                                System.out.print("\nEvent date (dd-mm-yyy): ");
                                String eventDate = scan.nextLine(); 
                                //ask for event time
                                System.out.print("\nEvent time (hh:mm): ");
                                String eventHour = scan.nextLine();

                                //create string type date object
                                String eventTime = eventDate + " " + eventHour + ":00";
                                //add event using created method
                                userProfile.addEvent(eventName, eventTime);

                                //write profile to text file
                                userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                                //display profile
                                System.out.println("\n" + displayProfileInfo(userProfile));
                                break;

                            case 'p': //Add Post
                                //ask for post
                                System.out.print("New post: ");
                                String newPost = scan.nextLine();
                                //add post using created method
                                userProfile.addTimelinePosts(newPost);
                                //write profile to text file
                                userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                                //display profile
                                System.out.println("\n" + displayProfileInfo(userProfile));
                                break;

                            case 'v': //view friend list
                                //check conditions to see if the user has friend yet
                                if (userProfile.friendsSize > 0){
                                    System.out.print("\n" + userProfile.showFriendsList());
                                    //show friends list using created method
                                } else {
                                    System.out.println("You currently have no friends.");
                                }
                                
                                break;
                            case 'f': //Add friend
                                System.out.print("Your friend's name: ");
                                String friend = scan.nextLine(); //ask for friend's name
                                
                                //add friend using created method
                                userProfile.addFriend(friend);

                                userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                                //write profile to text file

                                System.out.print("\n" + displayProfileInfo(userProfile));
                                //display profile
                                break;
                            case 'd': //delete friend
                                System.out.print("Your friend's email: ");
                                String friendDelete = scan.nextLine();

                                userProfile.removeFriend(friendDelete);

                                userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                                //write profile to text file

                                System.out.print("\n" + displayProfileInfo(userProfile));
                                break;

                            case 'e':
                                System.out.println("Session ended");
                                break;

                            default:
                                System.out.println("Invalid choice");
                        } 
                    } while (userChoice != 'e');
                
                    break;
                case 'l':
                /**
                 * Load existing profile from text file
                 */
                    userProfile = loadExistProfile();

                    userProfile.updateEventList();
                    userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");

                    System.out.print("\n" + displayProfileInfo(userProfile));

                    //These are the same switches as in the above case of 'r'
                    do
                    {
                        System.out.print("\n\nSelect your option: \nPost to timeline (p) \nAdd event (a) \nView friend list (v)");
                        System.out.print("\nAdd friend (f) \nDelete friend (d) \nLog out (e) \n\t ---->");
                        userChoice = getChar();
                        switch(userChoice)
                        {
                            case 'a': //Add Event
                                //ask for event name
                                System.out.print("Event name: ");
                                String eventName = scan.nextLine(); 
                                //ask for event date
                                System.out.print("\nEvent date (dd-mm-yyy): ");
                                String eventDate = scan.nextLine(); 
                                //ask for event time
                                System.out.print("\nEvent time (hh:mm): ");
                                String eventHour = scan.nextLine();

                                //create string type date object
                                String eventTime = eventDate + " " + eventHour + ":00";
                                //add event using created method
                                userProfile.addEvent(eventName, eventTime);

                                //write profile to text file
                                userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                                //display profile
                                System.out.println("\n" + displayProfileInfo(userProfile));
                                break;

                            case 'p': //Add Post
                                //ask for post
                                System.out.print("New post: ");
                                String newPost = scan.nextLine();
                                //add post using created method
                                userProfile.addTimelinePosts(newPost);
                                //write profile to text file
                                userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                                //display profile
                                System.out.println("\n" + displayProfileInfo(userProfile));
                                break;

                            case 'v': //view friend list
                                //check conditions to see if the user has friend yet
                                if (userProfile.friendsSize > 0){
                                    System.out.print("\n" + userProfile.showFriendsList());
                                    //show friends list using created method
                                } else {
                                    System.out.println("You currently have no friends.");
                                }
                                
                                break;
                            case 'f': //Add friend
                                System.out.print("Your friend's name: ");
                                String friend = scan.nextLine(); //ask for friend's name
                                
                                //add friend using created method
                                userProfile.addFriend(friend);

                                userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                                //write profile to text file

                                System.out.print("\n" + displayProfileInfo(userProfile));
                                //display profile
                                break;
                            case 'd': //delete friend
                                System.out.print("Your friend's email: ");
                                String friendDelete = scan.nextLine();

                                userProfile.removeFriend(friendDelete);

                                userProfile.writeToTextFile(displayProfileInfo(userProfile), "MySocialProfile.txt");
                                //write profile to text file

                                System.out.print("\n" + displayProfileInfo(userProfile));
                                break;

                            case 'e':
                                System.out.println("Session ended");
                                break;

                            default:
                                System.out.println("Invalid choice");
                        } 
                    } while (userChoice != 'e');


                    break;

                case 'q':
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.print("\nNot a valid entry.\n");
            }

        } while(userChoice != 'q');
    }

    public static String getString() throws IOException
	{
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}
	// -------------------------------------------------------------
	public static char getChar() throws IOException
	{
		String s = getString();
		return s.charAt(0);
	}
	//-------------------------------------------------------------
	public static int getInt() throws IOException
	{
		String s = getString();
		return Integer.parseInt(s);
	}
}


