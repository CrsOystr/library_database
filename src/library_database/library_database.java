//Written by Nicolas Metz for CS5530 - Spring 2015
//Base example of code provided by Professor Robert Christensen

package library_database;

import java.sql.*;
import java.util.Calendar;
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
			    	check_out();
			    	break;
			    case 3:
			    	wait_list();
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
	
	
	//Function for allowing users to check out books
	public static void check_out(){
		try{
			String isbn = null;
			boolean isbn_loop = true;
			//this loop ensures the isbn exists in the database
			while (isbn_loop)
			{
				System.out.println("Enter the isbn of the book to check out:");
		    	isbn = in.nextLine();
		    	String query = "SELECT * FROM BOOK_STOCK where isbn = ?";
		    	PreparedStatement query_statment = con.prepareStatement(query);
		    	query_statment.setString(1, "" + isbn);
		    	ResultSet rs1=query_statment.executeQuery();
		    	if (rs1.next()){
		    		isbn_loop = false;
		    	}
		    	else 
		    	{
		        	System.out.println("You can only check out books that are currently stocked in the library.");
		        	System.out.println("Hit enter to return to the main menu and add information about this book to the database.");
		        	in.nextLine();
		        	return;
		    	}
			}
			System.out.println("Enter userID to be added to waitlist:");
	    	String user_id = in.nextLine();
	    	
	    	String wait_query = "SELECT * FROM WAIT_LIST where isbn = ?";
	    	PreparedStatement query_stat = con.prepareStatement(wait_query);
	    	query_stat.setString(1, "" + isbn);
	    	ResultSet rs1=query_stat.executeQuery();
	    	//Will go into this block if a waitlist for the book exists
	    	if (rs1.next()){
	    		//inside this block we try to find if the user_id is the one waiting the longest
		    	String final_query = "SELECT user_id FROM WAIT_LIST where isbn = ? GROUP BY isbn HAVING min(wait_since)";
		    	PreparedStatement query = con.prepareStatement(final_query);
		    	query.setString(1, "" + isbn);
		    	ResultSet rs2=query_stat.executeQuery();
		    	rs2.next();
		    	int user_id2 = rs2.getInt(1);
		    	int user_id3 = Integer.parseInt(user_id);
		    	if (user_id3 == user_id2)
		    	{
		    		System.out.println("User " + user_id2 +" is first in line on the waitlist");

		    	}
		    	else
		    	{
		        	System.out.println("There are people waiting for this item in front of you, so it is currently not available to checkout"); 
		        	System.out.println("If you are not already on the waitlist hit enter to return to the main menu.");
		        	in.nextLine();
		        	return;
		    	}
	    	}
	    	
	    	//This section checks to make sure a book is available
	    	String check_query = "SELECT copy_number FROM BOOK_STOCK where isbn = ? and location <> 'checkedout' and location <> 'lost'";
	    	PreparedStatement check_state= con.prepareStatement(check_query);
	    	check_state.setString(1, isbn);
	    	ResultSet check_result = check_state.executeQuery();
	    	if (check_result.next()){
		    	String copy_number = check_result.getString("copy_number");
		    	String query3 = "INSERT INTO CHECK_OUT (user_id, isbn, copy_number, due_date) "
		    			+ "VALUES(?,?,?,?)";
		    	PreparedStatement state3 = con.prepareStatement(query3);
		    	state3.setString(1, user_id);
		    	state3.setString(2, isbn);
		    	state3.setString(3, copy_number);
		    	long time = System.currentTimeMillis();
		    	java.sql.Date date = new java.sql.Date(time);
		    	Calendar cal = Calendar.getInstance();
		    	cal.setTime(date);
		    	cal.add(Calendar.DAY_OF_YEAR,30);
		    	java.sql.Date date1 = new java.sql.Date(cal.getTimeInMillis());
		    	state3.setDate(4,date1);
		    	//System.out.println(state3);
		    	state3.executeUpdate();
		    	
		    	String query4 = "UPDATE BOOK_STOCK SET location = 'checkedout' WHERE isbn = ? AND copy_number = ?";
		    	PreparedStatement state4= con.prepareStatement(query4);
		    	state4.setString(1, isbn);
		    	state4.setString(2, copy_number);
		    	//System.out.println(state4);
		    	state4.executeUpdate();
		    	
		    	System.out.println("*Book ISBN " + isbn +" has been checked out by user " + user_id);
	    	}
	    	else{
	        	System.out.println("There are currently no copies of this book available for checkout"); 
	        	System.out.println("Hit enter to return to the  main menu.");
	        	in.nextLine();
	        	return;
	    	}
	    	
		}
		catch(Exception e){
    		System.out.println("Something went wrong checking out the book");
		}
		//Forces user to hit enter to return home
		System.out.println("Please hit enter to return to the main menu");
    	in.nextLine();
    	return;
	}
	
	//Function for allowing users to be added to a waitlist
	public static void wait_list(){
		try{
			String isbn = null;
			boolean isbn_loop = true;
			//this loop ensures the isbn exists in the database
			while (isbn_loop)
			{
				System.out.println("Enter the isbn of the books to be waited for:");
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
		        	System.out.println("You can only wait for books that already exist in the library database.");
		        	System.out.println("Hit enter to return to the main menu and add information about this book to the database.");
		        	in.nextLine();
		        	return;
		    	}
			}
			System.out.println("Enter userID to be added to waitlist:");
	    	String user_id = in.nextLine();
	    	
			
	    	String wait_query = "INSERT INTO WAIT_LIST(user_id, isbn, wait_since) "
	    			+ "VALUES(?,?,?)";
	    	PreparedStatement wait_state = con.prepareStatement(wait_query);
	    	wait_state.setString(1, user_id);
	    	wait_state.setString(2, isbn);
	    	long time = System.currentTimeMillis();
	    	java.sql.Date date = new java.sql.Date(time);
	    	wait_state.setDate(3,date);
	    	wait_state.executeUpdate();
			
			
			
		}
		catch(Exception e){
    		System.out.println("Something went wrong adding to waitlist");
		}
		//Forces user to hit enter to return home
		System.out.println("Please hit enter to return to the main menu");
    	in.nextLine();
    	return;
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
	    	ResultSet rs2=query2.executeQuery();
    		int copy_start = 1;
	    	if(rs2.next()){
	    		copy_start = rs2.getInt(1) + 1;
	    	}
	    	for (int i = copy_start; i < copy_start + copies; i++)
	    	{
		    	String book_query = "INSERT INTO BOOK_STOCK(isbn, copy_number, location) "
		    			+ "VALUES(?,?,?)";
		    	PreparedStatement book_state = con.prepareStatement(book_query);
		    	book_state.setString(1, isbn);
		    	book_state.setInt(2, i);
		    	book_state.setString(3,location);
		    	book_state.executeUpdate();
		    	/* ALTERNATE WAY FOR REFERWENCE
		    	String book_sql = "INSERT INTO BOOK_STOCK(isbn, copy_number, location)"
		    			+ "VALUES('"+isbn+"','"+i+"','"+location+"')"; 
		    	System.out.println(book_sql);

				stmt.executeUpdate(book_sql);
		    	 */
	    	}
    		System.out.println("Copies have succesfully been added to the database");

		
		}
		catch(Exception e){
    		System.out.println("Something went wrong adding copies of books to database");
		}
		//Forces user to hit enter to return home
		System.out.println("Please hit enter to return to the main menu");
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
	    	System.out.println("Enter publisher for new library book:");
	    	String publisher = in.nextLine();
	    	System.out.println("Enter publication year of new library book:");
	    	String pub_year = in.nextLine();
	    	System.out.println("Enter format of new library book:");
	    	String format = in.nextLine();	
	    	System.out.println("Enter subject of new library book:");
	    	String book_subject = in.nextLine();
	    	System.out.println("Enter summary of new library book:");
	    	String summary = in.nextLine();
	    	
	    	
	    	String book_sql = "INSERT INTO BOOK_DIR(isbn, title, author, publisher, pub_year, book_format, book_subject, summary)"
	    			+ "VALUES('"+isbn+"','"+title+"','"+author+"','"+publisher+"','"+pub_year+"','"+format+"','"+book_subject+"','"+summary+"')"; 
	    	System.out.println(book_sql);

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
