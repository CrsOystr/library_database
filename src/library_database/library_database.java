//Written by Nicolas Metz for CS5530 - Spring 2015
//Base example of code provided by Professor Robert Christensen

package library_database;

import java.sql.*;
import java.util.Scanner;

public class library_database {

	static Statement stmt = null;
	static Connection con = null; 
	static Scanner in = null;
	
	//entry point to program
	public static void main(String[] args) {
		System.out.println("Welcome to the Metz Library Database");
		 try
		 {
			 //remember to replace all this info;;;;
		     String userName = "cs5530u13";
	         String password = "90a6snh1";
        	 String url = "jdbc:mysql://georgia.eng.utah.edu/cs5530db13";
        	 
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
		    		+ "Please enter the integer associated with your choice");
		   
			    in = new Scanner(System.in);
			    int choice = in.nextInt();
			    in.nextLine();
			    
			    switch (choice) {
			    case 1:
			    	new_user();
			    	break;
			    case 2:
			    	break;
			    case 3:
			    	break;	
			    case 4:
			    	break;
			    case 5:
			    	new_book();
			    	break;
			    case 6:
			    	add_books();
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
	
	//FUNCTION FOR ADDING COPIES OF BOOKS
	public static void add_books(){
		try{
			String isbn = null;
			boolean isbn_loop = true;
			//this loop ensures the isbn exists in the database
			while (isbn_loop)
			{
				System.out.println("Enter the isbn of the books you want to add to database:");
		    	isbn = in.nextLine();
		    	String query = "SELECT * FROM BOOK_DIR where isbn = ?";
		    	PreparedStatement query_statment = con.prepareStatement(query);
		    	query_statment.setString(1, "" + isbn);
		    	ResultSet rs1=query_statment.executeQuery();
		    	if (rs1.next()){
		    		isbn_loop = false;
		    	}
		    	else 
		    	{
		        	System.out.println("You can only add copies of books that already exist in the library database.");
		        	System.out.println("Hit enter to return to the main menu and add information about this book to the database.");
		        	in.nextLine();
		        	return;
		    	}
			}
	    	System.out.println("Enter the number of copies as an integer:");
	    	int copies = in.nextInt();
			in.nextLine();
	    	System.out.println("Enter the location these books will be located in:");
	    	String location = in.nextLine();
			
			
	    	String isbn_query = "SELECT MAX(copy_number) FROM BOOK_STOCK WHERE isbn = ? GROUP BY isbn";
	    	PreparedStatement query2 = con.prepareStatement(isbn_query);
	    	query2.setString(1, "" + isbn);
	    	
	    	

    		System.out.println(query2);
	    	ResultSet rs2=query2.executeQuery();
    		System.out.println(query2);
	    	rs2.next();
	    	int user_id = rs2.getInt(1) + 1;
			
		
		}
		catch(Exception e){
    		System.out.println("Something went wrong adding copies of books to database");
		}
		//Forces user to hit enter to return home
    	in.nextLine();
    	return;
	}
	
	//FUNCTION FOR ADDING NEW BOOK TO THE DATABASE
	public static void new_book(){
		try{
			String isbn = null;
			boolean isbn_loop = true;
			//this loop ensures the username is unique
			while (isbn_loop)
			{
				System.out.println("Enter the isbn of the book to add to database:");
		    	isbn = in.nextLine();
		    	String query = "SELECT * FROM BOOK_DIR where isbn = ?";
		    	PreparedStatement query_statment = con.prepareStatement(query);
		    	query_statment.setString(1, "" + isbn);
		    	ResultSet rs1=query_statment.executeQuery();
		    	if (rs1.next()){
		        	System.out.println("This isbn already exists in database: Please try another");
		    	}
		    	else 
		    	{
		    		isbn_loop = false;
		    	}
			}
			//handles all input for new book
	    	System.out.println("Enter title for new library book:");
	    	String title = in.nextLine();
	    	System.out.println("Enter author of new library book");
	    	String author = in.nextLine();
	    	System.out.println("Enter pulisher for new library book:");
	    	String publisher = in.nextLine();
	    	System.out.println("Enter publication year of new library book:");
	    	String pub_year = in.nextLine();
	    	System.out.println("Enter format of new library book:");
	    	String format = in.nextLine();	
	    	System.out.println("Enter subject of new library book:");
	    	String book_subject = in.nextLine();
	    	System.out.println("Enter summary of new library book:");
	    	String summary = in.nextLine();
	    	
	    	
	    	String book_sql = "INSERT INTO BOOK_DIR(isbn, title, author, publisher, pub_year, format, book_subject, summary)"
	    			+ "VALUES('"+isbn+"','"+title+"','"+author+"','"+publisher+"','"+pub_year+"','"+format+"','"+book_subject+"','"+summary+"')"; 

			stmt.executeUpdate(book_sql);
			System.out.println("*New book " + title +" has been added to the database. isbn: " + isbn + "*\n"
	    				+ "please hit the enter key to return to main menu");
		}
		catch(Exception e){
    		System.out.println("Something went wrong adding a new book to the database");
		}
		//Forces user to hit enter to return home
    	in.nextLine();
    	return;
	}
	
	
	
	//FUNCTION FOR ADDING A NEW USER TO THE DATABASE
	public static void new_user(){
		//Begin try
		try{
		String username = null;
		boolean name_loop = true;
		//this loop ensures the username is unique
		while (name_loop)
		{
			System.out.println("Enter username of new user:");
	    	username = in.nextLine();
	    	String query = "SELECT * FROM LIB_USER where username = ?";
	    	PreparedStatement query_statment = con.prepareStatement(query);
	    	query_statment.setString(1, "" + username);
	    	ResultSet rs1=query_statment.executeQuery();
	    	if (rs1.next()){
	        	System.out.println("Username is taken: Please try another");
	    	}
	    	else 
	    	{
	    		name_loop = false;
	    	}
		}
		//handles all input for user
    	System.out.println("Enter full name of new user:");
    	String name = in.nextLine();
    	System.out.println("Enter address of new user:");
    	String address = in.nextLine();
    	System.out.println("Enter phone number of new user:");
    	String phone = in.nextLine();
    	System.out.println("Enter email address of new user:");
    	String email = in.nextLine();

    	//Finds the current max ID number in the SQL database and increments it.
    	String ID_query = "SELECT MAX(user_id) FROM LIB_USER";
    	PreparedStatement query2 = con.prepareStatement(ID_query);
    	ResultSet rs2=query2.executeQuery();
    	rs2.next();
    	int user_id = rs2.getInt(1) + 1;

    	
    	String user_sql = "INSERT INTO LIB_USER(user_id, username, uname, address, email, phone)"
    			+ "VALUES('"+user_id+"','"+username+"','"+name+"','"+address+"','"+email+"','"+phone+"')"; 

		stmt.executeUpdate(user_sql);
		System.out.println("*New user " + username +" has been added to the database. UserID: " + user_id + "*\n"
    				+ "please hit the enter key to return to main menu");
    	}
    	catch(Exception e){
    		System.out.println("Something went wrong adding a new user");
    	}
		//Forces user to hit enter to return home
    	in.nextLine();
    	return;
	}
	
	//FUNCTION FOR CLOSING THE CONNECTION TO THE DATABASE
	public static void close(){
		try{
			stmt.close();
			in.close();
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
	 return;
	}
}
