import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.Class.*;

 class createdb
{
    static Connection con;
    public static Connection create() throws RuntimeException {
        final String driver_class="com.mysql.cj.jdbc.Driver";
        try
        {
            Class.forName(driver_class);
            String user="root";
            String password="Ninshujain@0";
            String url="jdbc:mysql://localhost:3306/hotel";

            con=DriverManager.getConnection(url,user,password);
        }
        catch (SQLException s)
        {
            s.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
}
class Roomlist{
    public void roomlist(){
                         try{

        Connection con = createdb.create();
        String q = "select * from room";
        PreparedStatement pstmt = con.prepareStatement(q);
       ResultSet rs = pstmt.executeQuery();
       while(rs.next()){
        String room = rs.getString("typ");
        String price = rs.getString("price");
       
        System.out.println( room+" "+"with price Rs."+price );
       }
        
    }
    catch(Exception e){
        e.printStackTrace();
    } 

        
    }
}

class Booking{
    public boolean bookstate(guest gs){
        boolean f =false;
         try{
        Connection con = createdb.create();
        String q = "insert into guests(name1, roomtype, checkindate, intervalofstay) values (?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(q);
        pstmt.setString(1, gs.getGuestName());
        pstmt.setString(2, gs.getRoom());
        pstmt.setString(3, gs.getCheckInDate());
         pstmt.setInt(4, gs.getNumNights());

        pstmt.executeUpdate();
        f = true;
    }
    catch(Exception e){
        e.printStackTrace();
    } 
    
    return f;
    }

}


class guest {
    private String guestName;
    private String room;
    private String checkInDate;
    private int numNights;
    private double totalCost;

    public guest(String guestName, String room, String checkInDate, int numNights) {
        this.guestName = guestName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.numNights = numNights;
       

    }

    

    public String getGuestName() {
        return guestName;
    }

    public String getRoom(){
        return room;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public int getNumNights() {
        return numNights;
    }

    public double getTotalCost() {
        int pricetype;
            try{

        Connection con = createdb.create();
        String q = "select * from room where typ = ?";
        PreparedStatement pstmt = con.prepareStatement(q);
        pstmt.setString(1, this.getRoom());
       ResultSet rs = pstmt.executeQuery();
       
        if(rs.next()){
         pricetype = rs.getInt("price");
        }
        else{
            pricetype =0;
        }
         totalCost = numNights*pricetype;
        return totalCost;
    }
       catch(Exception e){
         e.printStackTrace();
        } 
       return 0;
    
    }
}


class Manager{
    public void viewBooking() {
       System.out.println("Guestlist");
                 try{

       Connection con = createdb.create();
       String q = "select * from guests";
       PreparedStatement pstmt = con.prepareStatement(q);
       ResultSet rs = pstmt.executeQuery();
         while(rs.next()){
         String name = rs.getString("name1");
         String roomtype = rs.getString("roomtype");
         String checkInDate = rs.getString("checkindate");
         int intervalofst = rs.getInt("intervalofstay");
         System.out.println( name+", "+roomtype+", "+checkInDate+", interval of stay="+intervalofst );
        }
        
      }
       catch(Exception e){
          e.printStackTrace();
      } 

    }
}

public class hotel1 {
    public static void main(String[] args) {
       
        // Define a manager password
        final String managerPassword = "yourmanagerpassword";

        Scanner scanner = new Scanner(System.in);


        while (true) {
            guest gs = new guest(managerPassword, managerPassword , managerPassword, 0);
            System.out.println("\nHotel Management System Menu:");
            System.out.println("1. View Room Types and Prices");
            System.out.println("2. Make a Booking");
            System.out.println("3. View Bookings (Manager Only)");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\nRoom Types and Prices:");
                    Roomlist view = new Roomlist();
                    view.roomlist();
                    
                    break;

                case 2:
                    System.out.print("Enter guest name: ");
                    scanner.nextLine();  // Consume the newline character
                    String guestName = scanner.nextLine();

                    System.out.print("Enter the room type : ");
                    String roomType = scanner.nextLine();


                   
                        System.out.print("Enter check-in date (YYYY-MM-DD): ");
                        String checkInDate = scanner.next();
                        System.out.print("Enter the number of nights: ");
                        int numNights = scanner.nextInt();

                        guest gs1 = new guest(guestName, roomType, checkInDate, numNights);
                        Booking booking = new Booking();
                        boolean answer = booking.bookstate(gs1);
                      if(answer==true){ 

                        System.out.println("\nBooking Successful!");
                        System.out.println("Guest: " + gs1.getGuestName());
                        System.out.println("Room: " + gs1.getRoom());
                        System.out.println("Check-in Date: " + gs1.getCheckInDate());
                        System.out.println("Total Cost: $" + gs1.getTotalCost());
                      }        
                      else{
                        System.out.println("Something went wrong");
                      }  
                    break;

                case 3:
                    System.out.print("Enter the manager password: ");
                    String enteredPassword = scanner.next();

                    if (enteredPassword.equals(managerPassword)) {
                        System.out.println("\nList of Bookings:");
                        Manager manager = new Manager();
                        manager.viewBooking();
                    } 
                    else {
                        System.out.println("Incorrect password. Access denied.");
                    }
                    break;

                case 4:
                    System.out.println("Exiting Hotel Management System.");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
}

