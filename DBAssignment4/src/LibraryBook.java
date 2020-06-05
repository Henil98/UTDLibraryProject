/*Written by Henil Doshi for CS6360.004, assignment 4, starting October 26, 2019.
    NetID: hxd180025
 * 
 * This class is used to load the GUI of Library book Entry. It contain all the GUI controls, methods and Table to display the contents.
 */

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.PreparedStatement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.mysql.jdbc.CallableStatement;
import net.proteanit.sql.DbUtils;
import javax.swing.JOptionPane.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.swing.JTextArea;
import javax.swing.JScrollPane;
public class LibraryBook{

	private JFrame frame;
	private JTextField title_textField;
	private JTextField isbn_textField;
	private JTextField author_textField;
	private JTextField edition_textField;
	private JTextField description_textField;
	private JTextField ddsn_textField;
	private JTextField subject_textField;
	private JTextField numpage_textField;
    private JTextField numcopy_textField;
    private JTextField pubDate_textField;
	private JTextField lccn_textField;
	private JTextField category_textField;
	private JTextField status_textField;
	private JTextArea status_textArea;
	private JComboBox publisher_comboBox;
	private JComboBox location_comboBox;
	private JComboBox typeOfBook_comboBox;
	private JComboBox rareOrOriginal_comboBox;
	
	
    private JLabel lblIsbn;
    private JLabel lblTitle;
    private JLabel lblDdsn;
    private JLabel lblSubject;
    private JLabel lblLccn;
    private JLabel lblNumpage;
    private JLabel lblEdition;
    private JLabel lblNumcopy;
    private JLabel lblRareOrOriginal;
    private JLabel lblPublicationdate;
    private JLabel lblAuthor;
    private JLabel lblCategory;
    private JLabel lblPublisher;
    private JLabel lblTypeOfBook;
    private JLabel lblDescription;
    private JLabel lblLocation;
    
  private JTable data_table;

    /**
	 * Create the application.
	 */
	public LibraryBook() 
	{
		initialize();
	}
	
	/*
	 * Connect to database method.
     */
	public static void connectDB()
	{
    	 	try 
    	 	{
    	 		Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
                System.out.println("Success!");
                Statement stmt = conn.createStatement();
                conn.close();             
    	 	} 
    	 	catch (Exception ex) 
    	 	{
    	 		System.out.println("Error in connection: " + ex.getMessage());
    	 	}
     } 
	
	/*
	 * Method to check valid date.
	 */
	public boolean isThisDateValid(String dateToValidate, String dateFromat)
	{
		
		Date date;
		if(dateToValidate == null)
		{
			return false;
		}
		
		SimpleDateFormat sdformat = new SimpleDateFormat(dateFromat);
		sdformat.setLenient(false);
		
		try
		{
			
			//if not valid, it will throw ParseException
			date = sdformat.parse(dateToValidate);
			System.out.println(date);
		
		} 
		catch (ParseException e) 
		{
			
			e.printStackTrace();
			return false;
		}
		Date date_today = new Date();
		
		return date.before(date_today);
	}
    
	/*
	 * Method to check if values is numeric or not.
	 */
    public static boolean isNumeric(String str) 
    { 
    	  try 
    	  {  
    	    Integer.parseInt(str);  
    	    return true;
    	  } catch(NumberFormatException e)
    	  {  
    	    return false;  
    	  }  
    	}
    
    /*
     * Method to check valid ISBN
     */
    static boolean isValidISBN(String isbn) 
    { 

        if ( isbn == null )
        {
            return false;
        }

        
        //must be a 13 digit ISBN
        if ( isbn.length() != 13 )
        {
            return false;
        }
        
      //remove any hyphens
        isbn = isbn.replaceAll( "-", "" );

        try
        {
            int t = 0;
            for ( int i = 0; i < 12; i++ )
            {
                int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
                t += (i % 2 == 0) ? digit * 1 : digit * 3;
            }

            //checksum must be 0-9. If calculated as 10 then = 0
            int checksum = 10 - (t % 10);
            if ( checksum == 10 )
            {
                checksum = 0;
            }

            return checksum == Integer.parseInt( isbn.substring( 12 ) );
        }
        catch ( NumberFormatException nfe )
        {
            //to catch invalid ISBNs that have non-numeric characters in them
            return false;
        }
        }
	
    /*
     * Method which gets all the values of attributes of book table and adds it to ArrayList booklist.
     */
	public ArrayList<Book> getBookList()
	   {
	       ArrayList<Book> bookList = new ArrayList<Book>();
	       try 
	       {
	    	   Class.forName("com.mysql.jdbc.Driver");
               Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
               Statement stmt = conn.createStatement();
               ResultSet rs=stmt.executeQuery("select * from book"); 
               Book book;
               while(rs.next())
	           {
	        	   book = new Book(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getInt(9),rs.getString(10),rs.getString(11),rs.getString(12));
	               bookList.add(book);
	           }
               conn.close();
	       } 
	      catch (Exception e) 
	       {
	           e.printStackTrace();
	       }
	       return bookList;
	   }
	
	/*
	 * This method takes the data from getBookList method and shows all the rows of table in JTable.
	 */
	 public void showBookList()
	   {
	       ArrayList<Book> list = getBookList();
	       DefaultTableModel model = (DefaultTableModel)data_table.getModel();
	       Object[] row = new Object[12];
	       for(int i = 0; i < list.size(); i++)
	       {
	           row[0] = list.get(i).getB_isbn();
	           row[1] = list.get(i).getB_title();
	           row[2] = list.get(i).getB_ddsn();
	           row[3] = list.get(i).getB_subject();
	           row[4] = list.get(i).getB_lccn();
	           row[5] = list.get(i).getB_numpage();
	           row[6] = list.get(i).getB_edition();
	           row[7] = list.get(i).getB_description();
	           row[8] = list.get(i).getB_numcopy();
	           row[9] = list.get(i).getB_rare();
	           row[10] = list.get(i).getB_pubdate();
	           row[11] = list.get(i).getB_category();
	           System.out.println(row[11]);
	           model.addRow(row);
	       }
	    }

	
	 /**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setTitle("Library Book Entry");
		frame.setBounds(100, 100, 1500, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblIsbn = new JLabel("ISBN *");
		lblIsbn.setBounds(84, 13, 56, 16);
		frame.getContentPane().add(lblIsbn);
		
		JLabel lblTitle = new JLabel("Title *");
		lblTitle.setBounds(84, 53, 56, 16);
		frame.getContentPane().add(lblTitle);
		
		isbn_textField = new JTextField();
		isbn_textField.setBounds(214, 10, 217, 22);
		frame.getContentPane().add(isbn_textField);
		isbn_textField.setColumns(10);
		
		title_textField = new JTextField();
		title_textField.setBounds(214, 50, 217, 22);
		frame.getContentPane().add(title_textField);
		title_textField.setColumns(10);
		
		JLabel lblDdsn = new JLabel("Dewey Decimal System Number");
		lblDdsn.setBounds(12, 95, 194, 16);
		frame.getContentPane().add(lblDdsn);
		
		ddsn_textField = new JTextField();
		ddsn_textField.setBounds(214, 92, 217, 22);
		frame.getContentPane().add(ddsn_textField);
		ddsn_textField.setColumns(10);
		
		JLabel lblSubject = new JLabel("Subject *");
		lblSubject.setBounds(84, 139, 56, 16);
		frame.getContentPane().add(lblSubject);
		
		subject_textField = new JTextField();
		subject_textField.setBounds(214, 136, 217, 22);
		frame.getContentPane().add(subject_textField);
		subject_textField.setColumns(10);
		
		JLabel lblLccn = new JLabel("LibraryofCongressCatalogNumber ");
		lblLccn.setBounds(0, 182, 217, 16);
		frame.getContentPane().add(lblLccn);
		
		lccn_textField = new JTextField();
		lccn_textField.setBounds(214, 179, 217, 22);
		frame.getContentPane().add(lccn_textField);
		lccn_textField.setColumns(10);
		
		JLabel lblNumpage = new JLabel("Number of Pages *");
		lblNumpage.setBounds(47, 227, 116, 16);
		frame.getContentPane().add(lblNumpage);
		
		numpage_textField = new JTextField();
		numpage_textField.setBounds(214, 224, 217, 22);
		frame.getContentPane().add(numpage_textField);
		numpage_textField.setColumns(10);
		
		JLabel lblEdition = new JLabel("Edition");
		lblEdition.setBounds(70, 273, 56, 16);
		frame.getContentPane().add(lblEdition);
		
		edition_textField = new JTextField();
		edition_textField.setBounds(214, 270, 217, 22);
		frame.getContentPane().add(edition_textField);
		edition_textField.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description *");
		lblDescription.setBounds(47, 316, 79, 16);
		frame.getContentPane().add(lblDescription);
		
		description_textField = new JTextField();
		description_textField.setBounds(214, 313, 217, 22);
		frame.getContentPane().add(description_textField);
		description_textField.setColumns(10);
		
		JLabel lblNumcopy = new JLabel("Number of Copies *");
		lblNumcopy.setBounds(34, 363, 129, 16);
		frame.getContentPane().add(lblNumcopy);
		
		numcopy_textField = new JTextField();
		numcopy_textField.setBounds(214, 360, 217, 22);
		frame.getContentPane().add(numcopy_textField);
		numcopy_textField.setColumns(10);
		
		JLabel lblRareOrOriginal = new JLabel("Rare or Original *");
		lblRareOrOriginal.setBounds(47, 406, 139, 16);
		frame.getContentPane().add(lblRareOrOriginal);
		
		JComboBox rareOrOriginal_comboBox = new JComboBox();
		rareOrOriginal_comboBox.setBounds(214, 406, 217, 22);
		frame.getContentPane().add(rareOrOriginal_comboBox);
		rareOrOriginal_comboBox.addItem("No");
		rareOrOriginal_comboBox.addItem("Yes");

		
		JLabel lblPublicationdate = new JLabel("Publication Date *(YYYY-MM-DD)");
		lblPublicationdate.setBounds(515, 13, 202, 16);
		frame.getContentPane().add(lblPublicationdate);
		
		pubDate_textField = new JTextField();
		pubDate_textField.setBounds(751, 10, 217, 22);
		frame.getContentPane().add(pubDate_textField);
		pubDate_textField.setColumns(10);
		
		JLabel lblAuthor = new JLabel("Author(s) *");
		lblAuthor.setBounds(531, 50, 79, 16);
		frame.getContentPane().add(lblAuthor);
		
		author_textField = new JTextField();
		author_textField.setBounds(751, 50, 217, 22);
		frame.getContentPane().add(author_textField);
		author_textField.setColumns(10);
		
		JLabel lblCategory = new JLabel("Category");
		lblCategory.setBounds(541, 92, 56, 16);
		frame.getContentPane().add(lblCategory);
		
		category_textField = new JTextField();
		category_textField.setBounds(751, 92, 217, 22);
		frame.getContentPane().add(category_textField);
		category_textField.setColumns(10);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(595, 338, 56, 16);
		frame.getContentPane().add(lblStatus);
		
		status_textField = new JTextField();
		status_textField.setBounds(751, 335, 704, 22);
		frame.getContentPane().add(status_textField);
		status_textField.setColumns(10);
		status_textField.setText("Insert mode");
		status_textField.setEditable(false);
		
		JLabel lblPublisher = new JLabel("Publisher *");
		lblPublisher.setBounds(541, 139, 91, 16);
		frame.getContentPane().add(lblPublisher);
		
		JComboBox publisher_comboBox = new JComboBox();
		publisher_comboBox.setBounds(751, 139, 217, 22);
		frame.getContentPane().add(publisher_comboBox);
		
		//Loading the publisher values from the database into the combobox.
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
            String sql_publisher = "SELECT publisherName FROM publisher";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql_publisher);
            while(rs.next())
            {
            	publisher_comboBox.addItem(rs.getString(1));
            }
            conn.close();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		JLabel lblTypeOfBook = new JLabel("Type of Book *");
		lblTypeOfBook.setBounds(541, 182, 110, 16);
		frame.getContentPane().add(lblTypeOfBook);
		
		JComboBox typeOfBook_comboBox = new JComboBox();
		typeOfBook_comboBox.setBounds(751, 179, 217, 22);
		frame.getContentPane().add(typeOfBook_comboBox);
		
		//Loading the type of Book values from the database into the combobox.
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
            String sql_typebook = "SELECT itemType FROM typeofitem";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql_typebook);
            while(rs.next())
            {
            	typeOfBook_comboBox.addItem(rs.getString(1));
            }
            conn.close();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		JLabel lblLocation = new JLabel("Location *(FloorNumber Shelf Section)");
		lblLocation.setBounds(479, 227, 227, 16);
		frame.getContentPane().add(lblLocation);
		
		JComboBox location_comboBox = new JComboBox();
		location_comboBox.setBounds(751, 227, 217, 22);
		frame.getContentPane().add(location_comboBox);
		
		
		//Loading the location values from the database into the combobox.
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
            String sql_location = "SELECT floorNumber,shelf,section FROM location";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql_location);
            while(rs.next())
            {
            	location_comboBox.addItem(rs.getString(1)+"   "+rs.getString(2)+"   "+rs.getString(3));
            }
            conn.close();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 521, 1443, 319);
		frame.getContentPane().add(scrollPane);
		
		
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(47, 467, 97, 25);
		frame.getContentPane().add(btnSave);
		
		/*
		 * On the click of Save button, the values in the textfields and comboboxes will be inserted into the book table.
		 */
		btnSave.addActionListener(new ActionListener()
		 {
			 @Override
			 public void actionPerformed(ActionEvent e)
			 {
				 // TODO Auto-generated method stub
				 try
				 {
					 System.out.println("Button Pressed!");
					 
					 // isbn validation 
					 String isbn_data = isbn_textField.getText();
					 if (!isValidISBN(isbn_data)) {
						 System.out.println("ISBN Incoorect!");
						 JOptionPane.showMessageDialog(frame, "ISBN invalid. Please enter valid ISBN."+isbn_data);
						 isbn_textField.setText("");
						 status_textField.setText("Invalid input");
						 return;
					 }
					 
					 // title validation
					 if(title_textField.getText().length() < 1) {
						 System.out.println("Title cannot be empty");
						 JOptionPane.showMessageDialog(frame, "Title cannot be empty");
						 status_textField.setText("Invalid input");
						 return;
					 }

					 // number of pages validation
					 if(!isNumeric(numpage_textField.getText())) {
						 
						 System.out.println("Number of pages has to be numeric");
						 JOptionPane.showMessageDialog(frame, "Number of pages has to be numeric");
						 status_textField.setText("Invalid input");
						 numpage_textField.setText("");
						 return;
					 }

					 if(Integer.parseInt(numpage_textField.getText()) < 1) {
						 System.out.println("Number of pages cant be zero");
						 JOptionPane.showMessageDialog(frame, "Number of pages cant be zero");
						 status_textField.setText("Invalid input");
						 numpage_textField.setText("");
						 return;
					 }
					 
					 // number of copies validation
					 if(!isNumeric(numcopy_textField.getText())) {
						 
						 System.out.println("Number of copies has to be numeric");
						 JOptionPane.showMessageDialog(frame, "Number of copies has to be numeric");
						 status_textField.setText("Invalid input");
						 numcopy_textField.setText("");
						 return;
					 }

					 if(Integer.parseInt(numcopy_textField.getText()) < 1) {
						 System.out.println("Number of copies cant be zero");
						 JOptionPane.showMessageDialog(frame, "Number of copies cant be zero");
						 status_textField.setText("Invalid input");
						 numcopy_textField.setText("");
						 return;
					 }

					 for(char ch1: category_textField.getText().toCharArray()) {
						 if(!Character.isLetter(ch1)) {
							 if(ch1 != ' ') {
								 System.out.println("Category name invalid");
								 JOptionPane.showMessageDialog(frame, "Category name must contain alphabets only");
								 status_textField.setText("Invalid input");
								 category_textField.setText("");
								 return;
							 }
						 }
					 }
					 
	                 String author_data1 = author_textField.getText();
					 String[] authorset1 = author_data1.split(",");
					 
					 for(String temp_author:authorset1) {
						 for(char ch1: temp_author.toCharArray()) {
							 if(!Character.isLetter(ch1)) {
								 if(ch1 != ' ') {
									 System.out.println("Author name invalid");
									 JOptionPane.showMessageDialog(frame, "Author name must contain alphabets only");
									 status_textField.setText("Invalid input");
									 author_textField.setText("");
									 return;
								 }
							 }
						 }
					 }
					 
					 // check for empty author names
					 for(String temp_author:authorset1) {
						 if(temp_author.length()<1) {
							 System.out.println("Author name empty");
							 JOptionPane.showMessageDialog(frame, "Author name cannot be empty");
							 status_textField.setText("Invalid input");
							 author_textField.setText("");
							 return;
						 }
					 }


					 // publication date validation
					 if(!isThisDateValid(pubDate_textField.getText(),"yyyy-MM-dd")) {
						 System.out.println("Publication date invalid format");
						 JOptionPane.showMessageDialog(frame, "Publication date invalid format");
						 status_textField.setText("Invalid input");
						 pubDate_textField.setText("");
						 return;
					 }

					 // edition validation
					 if(edition_textField.getText().length()>0) {
						 if(!isNumeric(edition_textField.getText())) {
							 
							 System.out.println("Edition has to be numeric");
							 JOptionPane.showMessageDialog(frame, "Edition has to be numeric");
							 status_textField.setText("Invalid input");
							 edition_textField.setText("");
							 return;
						 }
					 }

					 Class.forName("com.mysql.jdbc.Driver");
	                 Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
	                
	                 String isbn_check = isbn_textField.getText();
	                 String sql_check_isbn = "SELECT count(*) FROM book WHERE isbn='"+isbn_check+"'";
	                 Statement stmt_isbncheck = conn.createStatement();
	                 ResultSet rs_isbncheck = stmt_isbncheck.executeQuery(sql_check_isbn);
	                 
	                 int count=0;
	                 while(rs_isbncheck.next())
	                 {
	                	 count = rs_isbncheck.getInt(1);
	                 }
	                 if(count!=0)
	                 {
	                	 
	                	 JOptionPane.showMessageDialog(frame, "This is book is already in library, check ISBN!");
	                 }
	                 else
	                 {
	                	 String author_data = author_textField.getText();
						 String loc_data = String.valueOf(location_comboBox.getSelectedItem());
						 String pub_data = String.valueOf(publisher_comboBox.getSelectedItem());
						 String typebook_data = String.valueOf(typeOfBook_comboBox.getSelectedItem());
						 String[] locset = loc_data.split("   ");
						 
						 String sql_location = "SELECT location_id FROM location where floorNumber='"+locset[0]+"'and shelf='"+locset[1]+"'and section='"+locset[2]+"'";
						 
				         Statement stmt_loc = conn.createStatement();
				         String sql_publisher = "SELECT publisherID FROM publisher where publisherName='"+pub_data+"'";
				         Statement stmt_pub = conn.createStatement();
				         String sql_typebook = "SELECT typeOfItemID FROM typeofitem where itemType='"+typebook_data+"'";
				         
				         
				         Statement stmt_typebook = conn.createStatement();
				         ResultSet rs_pub = stmt_pub.executeQuery(sql_publisher);
				         ResultSet rs_loc = stmt_loc.executeQuery(sql_location);
				         ResultSet rs_typebook = stmt_typebook.executeQuery(sql_typebook);
				         int pub_ID = 0;
				         int loc_ID=0;
				         int typebook_ID=0;
				         while(rs_pub.next())
				         {
				        	 pub_ID = rs_pub.getInt(1);
				         }
				         while(rs_loc.next())
				         {
				        	 loc_ID = rs_loc.getInt(1);
				        	 System.out.println(loc_ID);
				         }
				         while(rs_typebook.next())
				         {
				        	 typebook_ID = rs_typebook.getInt(1);
				        	 System.out.println(typebook_ID);
				         }
				         
				         
		                 
						 String sql_book = "INSERT INTO book(isbn,title,deweyDecimalSystemNumber,subject,libraryOfCongressCatalogNumber,"
						 		+ "numberOfPages,edition,description,numberOfCopies,rareOrOriginal,publicationDate,category,bookType_ID,bookLoc_ID,bookPub_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						 String sql_book_isbn = "select LAST_INSERT_ID()"; 
						 PreparedStatement pstmt_book = conn.prepareStatement(sql_book);
						 PreparedStatement pstmt_book_isbn = conn.prepareStatement(sql_book_isbn);
						 pstmt_book.setString(1, (isbn_textField.getText()));
						 pstmt_book.setString(2, title_textField.getText());
						 pstmt_book.setString(3, ddsn_textField.getText());
						 pstmt_book.setString(4, subject_textField.getText());
						 pstmt_book.setString(5, lccn_textField.getText());
						 pstmt_book.setString(6, numpage_textField.getText());
						 pstmt_book.setString(7, edition_textField.getText());
						 pstmt_book.setString(8, description_textField.getText());
						 if(typeOfBook_comboBox.getSelectedItem().toString() == "Ebook")
						 {
							 pstmt_book.setInt(9,1);
						 }	 
						 pstmt_book.setInt(9, Integer.parseInt(numcopy_textField.getText()));
						 pstmt_book.setString(10, String.valueOf(rareOrOriginal_comboBox.getSelectedItem()));
						 pstmt_book.setDate(11, java.sql.Date.valueOf(pubDate_textField.getText()));
						 pstmt_book.setString(12, category_textField.getText());
						 pstmt_book.setInt(13, typebook_ID);
						 pstmt_book.setInt(14, loc_ID);
						 pstmt_book.setInt(15, pub_ID);
						 pstmt_book.executeUpdate();
						 ResultSet rs = pstmt_book_isbn.executeQuery();
						 while(rs.next())
						 {
							 int isbn_id =  ((Number) rs.getObject(1)).intValue();
							 System.out.println("isbn last inserted:" + isbn_id); 
						 }
						 
						 
						 int id= 0;
						 String[] authorset = author_data.split(",");
						 for(int i=0; i<authorset.length; i++)
						 {
							 String sql_author = "INSERT INTO author(authorName) VALUES(?)";
							 PreparedStatement pstmt_author = conn.prepareStatement(sql_author,Statement.RETURN_GENERATED_KEYS);
							 pstmt_author.setString(1, authorset[i]);
							 pstmt_author.executeUpdate();
							 String sql_getid = "SELECT authorID from author where authorName='"+authorset[i]+"'";
					            Statement st = conn.createStatement();
					            ResultSet rs12 = st.executeQuery(sql_getid);
					            while(rs12.next())
					            {
					            	id=rs12.getInt(1);
					            }
							 
							 String sql_authorhaswritten = "INSERT INTO authorhaswritten(author_ID,isbn) VALUES(?,?)";
							 PreparedStatement pstmt_authorhaswritten = conn.prepareStatement(sql_authorhaswritten);
							 pstmt_authorhaswritten.setInt(1, id);
							 pstmt_authorhaswritten.setString(2, isbn_check);
							 pstmt_authorhaswritten.executeUpdate();
						 }
						String query = "select * from book";
						PreparedStatement pst = conn.prepareStatement(query);
						ResultSet rs_showdata = pst.executeQuery();
						DefaultTableModel model = (DefaultTableModel)data_table.getModel();
				        model.setRowCount(0);
				        showBookList();
				        status_textField.setEditable(true);
				        status_textField.setText("Book inserted.");
				        status_textField.setEditable(false);
	                 }
	            }
				 catch (Exception e1)
				{
					e1.printStackTrace();
				}
			 }
		 });
					 
					

		 data_table = new JTable();
			scrollPane.setViewportView(data_table);

		 data_table.setModel(new DefaultTableModel(new Object [][] {},
			 new String[] {"isbn","title","deweyDecimalSystemNumber","subject","libraryOfCongressCatalogNumber","numberOfPages","edition","description","numberOfCopies","rareOrOriginal",
					 "publicationDate","category"}) {
			 /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
               return false;
			 }
		 });
		 
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(190, 467, 97, 25);
		frame.getContentPane().add(btnSearch);
		
		/*
		 * Search button which searches the values by ISBN, title, DDSN, LCCN, subject, description, category.
		 */
		btnSearch.addActionListener(new ActionListener()
		{
			@Override
            public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				try
				{
					//Connection to DB
					Class.forName("com.mysql.jdbc.Driver");
	                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
	                
	                //DefaultTableModel model = (DefaultTableModel)data_table.getModel();
			        //model.setRowCount(0);
			        showBookList();
			        
			        status_textField.setEditable(true);
			        status_textField.setText("Search Mode.");
			        status_textField.setEditable(false);
	                
	                String title=title_textField.getText();
	                String isbn=isbn_textField.getText();
	                String ddsn=ddsn_textField.getText();
	                String subject=subject_textField.getText();
	                String category=category_textField.getText();
	                String description=description_textField.getText();
	                String lccn=lccn_textField.getText();
	                
	                if(title.isEmpty())
	                {
	                	title="-1";
	                }
	                if(lccn.isEmpty())
	                {
	                	lccn="-1";
	                }
	                if(description.isEmpty())
	                {
	                	description="-1";
	                }
	                if(category.isEmpty())
	                {
	                	category="-1";
	                }
	                if(isbn.isEmpty())
	                {
	                	isbn="-1";
	                }
	                if(subject.isEmpty())
	                {
	                	subject="-1";
	                }
	                if(ddsn.isEmpty())
	                {
	                	ddsn="-1";
	                }
	                
	               String search_sql="select isbn,title,deweyDecimalSystemNumber,subject,libraryOfCongressCatalogNumber,numberOfPages,"
	                		+ "edition,description,numberOfCopies,rareOrOriginal,publicationDate,category"
	                		+ " from book as b inner join "
	                		+ "typeofitem as t on b.bookType_ID=t.typeOfItemID inner join location as l on b.bookLoc_ID=l.location_id "
	                		+ "inner join publisher as p on b.bookPub_ID=p.publisherID where (isbn='"+isbn+"' OR"
	                		+ " '"+isbn+"'='-1') AND (title like '%"+title+"%' OR  '"+title+"'='-1') AND "
	                		+ "(category='"+category+"' OR  '"+category+"'='-1') AND "
	                		+ "(description='"+description+"' OR  '"+description+"'='-1') AND "
	                		+ "(libraryOfCongressCatalogNumber='"+lccn+"' OR  '"+lccn+"'='-1') AND "
	                		+ "(deweyDecimalSystemNumber='"+ddsn+"' OR  '"+ddsn+"'='-1') AND "
	                		+ "(subject like '%"+subject+"%' OR  '"+subject+"'='-1')";
	                
	                PreparedStatement pst = conn.prepareStatement(search_sql);
					ResultSet rs = pst.executeQuery();
					data_table.setModel(DbUtils.resultSetToTableModel(rs));

				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
				
			}
		});
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(349, 467, 97, 25);
		frame.getContentPane().add(btnClear);
		
		/*
		 * Clear button which clears the values in the textfields.
		 */
		btnClear.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				status_textField.setEditable(true);
		        status_textField.setText("Data Cleared.");
		        status_textField.setEditable(false);
				isbn_textField.setEditable(true);
				title_textField.setText("");
				isbn_textField.setText("");
				author_textField.setText("");
				edition_textField.setText("");
				description_textField.setText("");
				ddsn_textField.setText("");
				lccn_textField.setText("");
				subject_textField.setText("");
				category_textField.setText("");
				numpage_textField.setText("");
				pubDate_textField.setText("");
				numcopy_textField.setText("");
				rareOrOriginal_comboBox.setSelectedIndex(0);
				publisher_comboBox.setSelectedIndex(0);
				typeOfBook_comboBox.setSelectedIndex(0);
				location_comboBox.setSelectedIndex(0);
			}
		});
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(492, 467, 116, 25);
		frame.getContentPane().add(btnUpdate);
		
		/*
		 * On clicking on entry of data table, the textbox fields will be filled with the values that are in that row according to attribute
		 * names and table automatically goes to update mode.
		 */
		data_table.addMouseListener(new MouseAdapter() 
		{
		      public void mouseClicked(MouseEvent e) 
		      {
		    	  status_textField.setEditable(true);
			      status_textField.setText("Update Mode");
			      status_textField.setEditable(false);
			      
		    	  int i = data_table.getSelectedRow();
		    	  TableModel model = data_table.getModel();
		          
		    	  // Display Selected Row In JTextFields
		          isbn_textField.setText(model.getValueAt(i,0).toString());
		          isbn_textField.setEditable(false);
		          title_textField.setText(model.getValueAt(i,1).toString());
		          ddsn_textField.setText(model.getValueAt(i,2).toString());
		          subject_textField.setText(model.getValueAt(i,3).toString());
		          lccn_textField.setText(model.getValueAt(i,4).toString());
		          numpage_textField.setText(model.getValueAt(i,5).toString());
		          edition_textField.setText(model.getValueAt(i,6).toString());
		          description_textField.setText(model.getValueAt(i,7).toString());
		          numcopy_textField.setText(model.getValueAt(i,8).toString());
		          rareOrOriginal_comboBox.setSelectedItem(model.getValueAt(i,9).toString());
		          pubDate_textField.setText(model.getValueAt(i,10).toString());
		          category_textField.setText(model.getValueAt(i,11).toString());
		          
		          String isbn = isbn_textField.getText();
		          try
					{
						Class.forName("com.mysql.jdbc.Driver");
		                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
		                String authorset="";	
		                String final_authorset="";
		                int pub_ID= 0;
		                int loc_ID=0;
				        int typebook_ID=0;
				        String locationName="";
		                String sql_author = "SELECT distinct authorName FROM author as a inner join authorhaswritten as aw on a.authorID=aw.author_ID where isbn='"+isbn+"'";
				        Statement stmt_author = conn.createStatement();
				        ResultSet rs = stmt_author.executeQuery(sql_author);
		                while(rs.next())
		                 {
		                	 authorset = authorset + rs.getString(1) + ",";
		                 }	
		                
		                if (authorset.endsWith(","))
		                {
		                	final_authorset = authorset.substring(0,authorset.length()-1);
		                	author_textField.setText(final_authorset);
		                }
		                else
		                {
		                	author_textField.setText(authorset);
		                }
		                System.out.println(final_authorset);
		                
		                
		                String sql_pub = "SELECT bookPub_ID FROM book where isbn='"+isbn+"'";
				        Statement stmt_pub = conn.createStatement();
				        ResultSet rs_pub = stmt_pub.executeQuery(sql_pub);
				        while(rs_pub.next())
				         {
				        	 pub_ID = rs_pub.getInt(1);
				         }
				        String sql_pubname = "SELECT publisherName FROM publisher where publisherID='"+pub_ID+"'";
				        Statement stmt_pubname = conn.createStatement();
				        ResultSet rs_pubname = stmt_pubname.executeQuery(sql_pubname);
				        while(rs_pubname.next())
				         {
				        	publisher_comboBox.setSelectedItem(rs_pubname.getString(1).toString());
				         }
				        
				        String sql_loc = "SELECT bookLoc_ID FROM book where isbn='"+isbn+"'";
				        Statement stmt_loc = conn.createStatement();
				        ResultSet rs_loc = stmt_loc.executeQuery(sql_loc); 
				        while(rs_loc.next())
				         {
				        	 loc_ID = rs_loc.getInt(1);
				         }
				        String sql_locname = "SELECT floorNumber,shelf,section FROM location where location_id='"+loc_ID+"'";
				        Statement stmt_locname = conn.createStatement();
				        ResultSet rs_locname = stmt_locname.executeQuery(sql_locname);
				        while(rs_locname.next())
				         {
				        	locationName= rs_locname.getString(1)+"   "+rs_locname.getString(2)+"   "+rs_locname.getString(3);
				         }
				        System.out.println(locationName);
				        location_comboBox.setSelectedItem(locationName.toString());
				         
				         String sql_typebook = "SELECT bookType_ID FROM book where isbn='"+isbn+"'";
				         Statement stmt_typebook = conn.createStatement();
				         ResultSet rs_typebook = stmt_typebook.executeQuery(sql_typebook);
				         while(rs_typebook.next())
				         {
				        	 typebook_ID = rs_typebook.getInt(1);
				         }
				         String sql_typename = "SELECT itemType FROM typeofitem where typeOfItemID='"+typebook_ID+"'";
					     Statement stmt_typename = conn.createStatement();
					     ResultSet rs_typename = stmt_typename.executeQuery(sql_typename);
					     while(rs_typename.next())
					     {
					        typeOfBook_comboBox.setSelectedItem(rs_typename.getString(1).toString());
					     }
					}
			      catch(Exception e1)
					{
						e1.printStackTrace();
					}
		      }
		    });
		
		
		/*
		 * Update button to update the values in the table. After clicking it, table will be filled with all the data, and then click on 
		 * particular entry, and do the changes you want to do and click update and all the updates will be done. 
		 */
		btnUpdate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				status_textField.setEditable(true);
		        status_textField.setText("Update Mode: Click any entry from table which you want to update.");
		        status_textField.setEditable(false);
				try
				{
					Class.forName("com.mysql.jdbc.Driver");
	                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/utdlibrary","root","root");
	                
	                DefaultTableModel model = (DefaultTableModel)data_table.getModel();
			        model.setRowCount(0);
			        showBookList();
	                
	                 String isbn_check = isbn_textField.getText();
	                 String sql_check_isbn = "SELECT count(*) FROM book WHERE isbn='"+isbn_check+"'";
	                 Statement stmt_isbncheck = conn.createStatement();
	                 ResultSet rs_isbncheck = stmt_isbncheck.executeQuery(sql_check_isbn);
	                 
	                 int count=0;
	                 while(rs_isbncheck.next())
	                 {
	                	 count = rs_isbncheck.getInt(1);
	                 }
	                 
					 String query = "select * from book";
					 String author_data = author_textField.getText();
					 String loc_data = String.valueOf(location_comboBox.getSelectedItem());
					 String pub_data = String.valueOf(publisher_comboBox.getSelectedItem());
					 String typebook_data = String.valueOf(typeOfBook_comboBox.getSelectedItem());
					 String[] locset = loc_data.split("   ");
					 
					 String sql_location = "SELECT location_id FROM location where floorNumber='"+locset[0]+"'and shelf='"+locset[1]+"'and section='"+locset[2]+"'";
					 Statement stmt_loc = conn.createStatement();
			         String sql_publisher = "SELECT publisherID FROM publisher where publisherName='"+pub_data+"'";
			         Statement stmt_pub = conn.createStatement();
			         String sql_typebook = "SELECT typeOfItemID FROM typeofitem where itemType='"+typebook_data+"'";
			         Statement stmt_typebook = conn.createStatement();
			         ResultSet rs_pub = stmt_pub.executeQuery(sql_publisher);
			         ResultSet rs_loc = stmt_loc.executeQuery(sql_location);
			         ResultSet rs_typebook = stmt_typebook.executeQuery(sql_typebook);
			         int pub_ID = 0;
			         int loc_ID=0;
			         int typebook_ID=0;
			         while(rs_pub.next())
			         {
			        	 pub_ID = rs_pub.getInt(1);
			         }
			         while(rs_loc.next())
			         {
			        	 loc_ID = rs_loc.getInt(1);
			        	 System.out.println(loc_ID);
			         }
			         while(rs_typebook.next())
			         {
			        	 typebook_ID = rs_typebook.getInt(1);
			        	 System.out.println(typebook_ID);
			         }
					String sql_book = "UPDATE book set title= ?,deweyDecimalSystemNumber=?,subject=?,"
							+ "libraryOfCongressCatalogNumber=?,numberOfPages=?,edition=?,description=?,numberOfCopies=?,rareOrOriginal=?,"
							+ "publicationDate=?,category=?,bookType_ID=?,bookLoc_ID=?,bookPub_ID=? where isbn = ?";
					
					 PreparedStatement pstmt_book = conn.prepareStatement(sql_book);
					
					
					 pstmt_book.setString(1, title_textField.getText());
					 pstmt_book.setString(2, ddsn_textField.getText());
					 pstmt_book.setString(3, subject_textField.getText());
					 pstmt_book.setString(4, lccn_textField.getText());
					 pstmt_book.setString(5, numpage_textField.getText());
					 pstmt_book.setString(6, edition_textField.getText());
					 pstmt_book.setString(7, description_textField.getText());
					 pstmt_book.setInt(8, Integer.parseInt(numcopy_textField.getText()));
					 pstmt_book.setString(9, String.valueOf(rareOrOriginal_comboBox.getSelectedItem()));
					 pstmt_book.setDate(10, java.sql.Date.valueOf(pubDate_textField.getText()));
					 pstmt_book.setString(11, category_textField.getText());
					 pstmt_book.setInt(12, typebook_ID);
					 pstmt_book.setInt(13, loc_ID);
					 pstmt_book.setInt(14, pub_ID);
					 pstmt_book.setString(15, (isbn_textField.getText()));
					 pstmt_book.executeUpdate();
					
					 int id= 0;
					 String[] authorset = author_data.split(",");
					 for(int i=0; i<authorset.length; i++)
					 {
						 String sql_author = "INSERT INTO author(authorName) VALUES(?)";
						 PreparedStatement pstmt_author = conn.prepareStatement(sql_author,Statement.RETURN_GENERATED_KEYS);
						 pstmt_author.setString(1, authorset[i]);
						 pstmt_author.executeUpdate();
						 String sql_getid = "SELECT authorID from author where authorName='"+authorset[i]+"'";
				            Statement st = conn.createStatement();
				            ResultSet rs12 = st.executeQuery(sql_getid);
				            while(rs12.next())
				            {
				            	id=rs12.getInt(1);
				            }
					 }
						 
				         String authdelete = "DELETE from authorhaswritten where isbn='"+isbn_check+"'";
				         Statement stmt_authdelete = conn.createStatement();
				         stmt_authdelete.executeUpdate(authdelete);
				         for(int i=0; i<authorset.length;i++)
				         {
							 String sql_authorhaswritten = "INSERT INTO authorhaswritten(author_ID,isbn) VALUES(?,?)";
							 PreparedStatement pstmt_authorhaswritten = conn.prepareStatement(sql_authorhaswritten);
							 pstmt_authorhaswritten.setInt(1, id);
							 pstmt_authorhaswritten.setString(2, isbn_check);
							 pstmt_authorhaswritten.executeUpdate();
				         }
					DefaultTableModel model1 = (DefaultTableModel)data_table.getModel();
			        model1.setRowCount(0);
			        showBookList();
			        
			        status_textField.setEditable(true);
			        status_textField.setText("Data Updated.");
			        status_textField.setEditable(false);
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try {
					LibraryBook window = new LibraryBook();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		try 
		{
			connectDB();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
