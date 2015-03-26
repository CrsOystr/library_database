//Written by Nicolas Metz for CS5530 - Spring 2015
//Base example of code provided by Professor Robert Christensen

package library_database;

import java.sql.*;
import java.util.Scanner;

public class library_database {

	static Statement stmt = null;
	static Connection con = null; 
	
	//entry point to program
	public static void main(String[] args) {
		System.out.println("Welcome to the Metz Library Database");
		//Connection con = null;
        
		 try
		 {
			 //remember to replace all this info;;;;
		     String userName = "cs5530";
	         String password = "cs5530_Spring2015";
        	 String url = "jdbc:mysql://georgia.eng.utah.edu/cs5530db";
        	 
        	 //Does all the connection to our database
	         Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        	 con = DriverManager.getConnection (url, userName, password);
	         System.out.println ("Database connection established\n" );
	         stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			    
	         
	         boolean looping = true;
			 while(looping){
		    // how to do query which take user input?
		    System.out.println("Welcome to the main menu, what do you wish to do?\n"
		    		+ "1. Create new library user\n"
		    		+ "2. Check out books\n"
		    		+ "3. Add user to waitlist\n"
		    		+ "4. Print user record\n"
		    		+ "5. Add book to database\n"
		    		+ "6. Add copies of book to database\n"
		    		+ "7. View late books\n"
		    		+ "8. Add book review\n"
		    		+ "9. Search for book\n"
		    		+ "10. Return a book\n"
		    		+ "11. Print book record \n"
		    		+ "12. Get book statistics\n"
		    		+ "13. Get user statistics\n"
		    		+ "14. Exit the Database\n"
		    		+ "Please enter the integer associated with your choice\n");
		   
			    Scanner in = new Scanner(System.in);
			    int choice = in.nextInt();
			    
			    switch (choice) {
			    case 1:
			    	break;
			    case 2:
			    	break;
			    case 3:
			    	break;	
			    case 4:
			    	break;
			    case 5:
			    	break;
			    case 6:
			    	break;
			    case 7:
			    	break;
			    case 8:
			    	break;
			    case 9:
			    	break;
			    case 10:
			    	break;
			    case 11:
			    	break;
			    case 12:
			    	break;
			    case 13:
			    	break;
			    case 14:
			    	looping = false;
			    	break;
			    default:
			    	System.out.println("invalid choice, please enter the integer corresponding to your choice");
			    }
		    }
		    
		    
		    ////// OPTION 1 - DO NOT USE, BUT IT IS SOMETHING THAT CAN BE DONE ///////
		    /*String query_1 = "SELECT * FROM student where sname LIKE \'%" + name + "%\'";
		    
		    ResultSet rs1=stmt.executeQuery(query_1);
		    System.out.println("Results for query 1");
		    while(rs1.next())
		    {
		    	System.out.print("name:");
		    		System.out.print(rs1.getString("sname"));
		    }
		    System.out.println("");
		    */
		    
		    String name = "sam";
		    /////// OPTION 2 - USE THIS TECHNIQUE //////
		    String query_2 = "SELECT * FROM student where sname LIKE ?";
		    PreparedStatement query_2_statment = con.prepareStatement(query_2);
		    
		    query_2_statment.setString(1, "%" + name + "%");
		    ResultSet rs2=query_2_statment.executeQuery();
		    System.out.println("Results for query 2");
		    while(rs2.next())
		    {
		    	System.out.print("name:");
		    	System.out.print(rs2.getString("sname"));
		    }
		    System.out.println("");
		    
		    close();
      	}
     	 catch (Exception e)
	         {
     	     e.printStackTrace();
     		 System.err.println ("Cannot connect to database server");
	         }
     	 finally
	         {
     	     if (con != null)
	             {
     	         try
             	 {
	                 con.close ();
     	             System.out.println ("Database connection terminated");
	                 }
             	 catch (Exception e) { /* ignore close errors */ }
     	     }
	         }
	}
	
	
	
	
	
	public static void close(){
		try{
			stmt.close();
			}
		catch (Exception e)
			{
			e.printStackTrace();
			System.err.println ("Error Closing Database");
         	}
		finally
			{
				if (con != null)
			{
 	         try
         	 	{
 	        	 	con.close ();
 	        	 	System.out.println ("Database connection terminated");
                 }
         	 catch (Exception e) { /* ignore close errors */ }
 	     }
         }	
	 
	 System.out.println("Program completed");
	}
}
