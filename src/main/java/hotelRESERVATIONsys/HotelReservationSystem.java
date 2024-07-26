package hotelRESERVATIONsys;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;


public class HotelReservationSystem 
{
	private static final String url="jdbc:mysql://localhost:3306/hotel_db";
	private static final String username="root";
	private static final String pw= "root";
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		try 
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
			}
		catch (ClassNotFoundException e)
			{
				System.out.println(e.getMessage());
			}
		try
		{
			Connection connection= DriverManager.getConnection(url,username,pw);
			while(true)
			{
				System.out.println();
				System.out.println("HOTEL MANAGEMENT SYSTEM");
				Scanner sc= new Scanner (System.in);
				System.out.println("1. Reserve a room");
				System.out.println("2. View Reservation");
				System.out.println("3. Get Room Number");
				System.out.println("4. Update Reservation");
				System.out.println("5. Delete Reservation");
				System.out.println("6. Exit main menu");
				int choice= sc.nextInt();
				switch(choice)
				{
				case 1:
					reserveRoom(connection,sc);
					break;
				case 2:
					viewReservations(connection);
					break;
				case 3:
					getRoomNumber(connection, sc);
					break;
				case 4:
					updateReservation(connection,sc);
					break;
				case 5:
					deleteReservation(connection,sc);
					break;
				case 6:
					exit(); //this may throw InterruptedException.
					sc.close();
					return;
				default:
					System.out.println("Invalid Choice, try again.");
				}
			}
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		catch(InterruptedException e)
		{
			throw  new RuntimeException(e);//this is explicitly thrown
		}
	}
	// when we create an instance of something try to use the instance as much time it is used and don't create the instance of same type, hence connection and sc are shared to increase the performance
	private static void reserveRoom(Connection connection, Scanner sc)
	{
		try
		{
			System.out.println("Enter Guest name: ");
			String guestName= sc.next();
			sc.nextLine();
			System.out.println("Enter room number: ");
			int roomNumber= sc.nextInt();
			System.out.println("Enter contact Number: ");
			String contactNumber = sc.next();
			
			String sql ="INSERT INTO reservation (guest_name, room_number, contact_number)"+ "VALUES ('" + guestName + "'," +roomNumber + ",'"+ contactNumber+"')";
			
			try(Statement statement= connection.createStatement())
			{
				int affectedRows= statement.executeUpdate(sql);
				
				if(affectedRows>0)
					System.out.println("Reservation successful!");
				else
					System.out.println("Reservation Failed.");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	private static void viewReservations(Connection connection)throws SQLException
	{
		//as no data is entered here then no need of Scanner class object hence it is not passed
		String sql= "select reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservation";
		//as we are getting the data hence we are using the resultSet interface.
		try (Statement statement = connection.createStatement();
				ResultSet resultSet= statement.executeQuery(sql))
		{
			System.out.println("Current Reservations:");
			System.out.println("+----------------+----------------------+-----------------+------------------------+-------------------------+");
			System.out.println("| Reservation ID | Guest                | Room Number     | Contact Number         | Reservation Date        |");
			System.out.println("+----------------+----------------------+-----------------+------------------------+-------------------------+");
			
			while(resultSet.next())//resultSet.next() returns boolean type of data, it will return true till it has data in it, else it would return false.
			{
				// the data written within () are the SQL table's field name
				int reservationId = resultSet.getInt("reservation_id");
				String guestName = resultSet. getString("guest_name");
				int roomNumber = resultSet.getInt("room_number");
				String contactNumber = resultSet.getString("contact_number");
				String reservationDate =  resultSet.getTimestamp("reservation_date").toString();
				
				//Format and display the reservation data in a table-like format
				System.out.printf("| %-14d | %-20s | %-15d | %-22s | %-23s |\n",
						reservationId, guestName, roomNumber, contactNumber, reservationDate );
			}
			System.out.println("+----------------+----------------------+-----------------+------------------------+-------------------------+");
		}
	}
	private static void getRoomNumber(Connection connection, Scanner sc)
	{
		try
		{
			System.out.println("Enter reservation ID: ");
			int reservationId = sc.nextInt();
			System.out.println("Enter the guest name: ");
			String guestName = sc.next();
			
			String sql= "SELECT room_number FROM reservation "+ "where reservation_id = "+reservationId +" and guest_name = ' "+ guestName + "'";
			
			try(Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(sql))
			{
				if(resultSet.next())
				{
					int roomNumber = resultSet.getInt("room_number");
					System.out.println("Room number for Reservation ID "+ reservationId +" and Guest "+ guestName + " is: "+roomNumber);
				}
				else
				{
					System.out.println("Reservation not found for the given ID and guest name.");
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void updateReservation(Connection connection, Scanner sc)
	{
		try 
		{
			System.out.println("Enter the reservation ID to update: ");
			int reservationId = sc.nextInt();
			sc.nextLine(); //used to consume the new line character
			//this will check if the reservation exists or not.
			if(!reservationExists(connection, reservationId))
			{
				System.out.println("Reservation not found for the given ID.");
				return;
			}
			System.out.println("Enter the new guest name: ");
			String newGuestName = sc.nextLine();
			System.out.println("Enter the new room number: ");
			int newRoomNumber = sc.nextInt();
			System.out.println("Enter the new contact number: ");
			String newContactNumber = sc.next();
			//the sql query.
			String sql= "UPDATE reservation set guest_name = '"+ newGuestName +"',"+"room_number ="+ newRoomNumber +", "+"contact_number = '"+newContactNumber + "' "+
			"Where reservation_id = "+ reservationId;
			try (Statement statement = connection.createStatement())
			{
				int affectedRows = statement.executeUpdate(sql);
				
				if(affectedRows > 0)
				{
					System.out.println("Reservation updated Successfully!");
				}
				else
				{
					System.out.println("Reservation update failed");
				}
			}
		}
		catch(SQLException e)
		{
			e.getStackTrace();
		}
	}
	
	private static void deleteReservation(Connection connection, Scanner sc)
	{
		try 
		{
			System.out.println("Enter reservation ID to delete: ");
			int reservationId = sc.nextInt();
			
			if(!reservationExists(connection,reservationId))
			{
				System.out.println("Reservation not found for the given ID.");
				return;
			}
			String sql = "DELETE from reservation where reservation_id = "  + reservationId;
			
			try(Statement statement = connection.createStatement())
			{
				int affectedRows = statement.executeUpdate(sql);
				
				if(affectedRows>0)
				{
					System.out.println("Reservation deleted successfully!");
				}
				else
				{
					System.out.println("Reservation deletion failed.");
				}
			}
		}
		catch(SQLException e)//for Statement Interface
		{
			e.getStackTrace();
		}
	}
	//this method will check if the reservation exists on the given reservation id.
	private static boolean reservationExists(Connection connection, int reservationId)
	{
		try {
            String sql = "SELECT reservation_id FROM reservation WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
	}
	//will exit the menu...
	public static void exit() throws InterruptedException {
	        System.out.print("Exiting System");
	        int i = 5;
	        while(i!=0)//just for loading style....
	        {
	        	System.out.print("👋🏻");
	            Thread.sleep(1000);
	            i--;
	        }
	        System.out.println();
	        System.out.println("ThankYou For Using Hotel Reservation System 🙏🏻");
	    }
}
