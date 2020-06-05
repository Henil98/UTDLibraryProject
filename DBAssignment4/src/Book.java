/*Written by Henil Doshi for CS6360.004, assignment 4, starting October 26, 2019.
    NetID: hxd180025
 * 
 * This class is used to get values of data columns which we are displaying in JTable. It is implemented in LibraryBook.java where it is used as
 * type of ArrayList.
 */
public class Book {

	private String b_isbn;
	private String b_title;
	private String b_ddsn;
	private String b_subject;
	private String b_lccn;
	private String b_numpage;
	private String b_edition;
	private String b_description;
	private int b_numcopy;
	private String b_rare;
	private String b_pubdate;
	private String b_category;
	
	/*
	 * Contructor to set the variables
	 */
	public Book(String b_isbn,String b_title,String b_ddsn,String b_subject,String b_lccn,String b_numpage,String b_edition,String b_description,int b_numcopy,String b_rare,String b_pubdate,String b_category)
	{
		this.b_isbn=b_isbn;
		this.b_title=b_title;
		this.b_ddsn=b_ddsn;
		this.b_subject=b_subject;
		this.b_lccn=b_lccn;
		this.b_numpage=b_numpage;
		this.b_edition=b_edition;
		this.b_description=b_description;
		this.b_numcopy=b_numcopy;
		this.b_rare=b_rare;
		this.b_pubdate=b_pubdate;
		this.b_category=b_category;
		
	}

	/*
	 * Getter methods of variables
	 */
	public String getB_isbn() {
		return b_isbn;
	}

	public String getB_title() {
		return b_title;
	}

	public String getB_ddsn() {
		return b_ddsn;
	}

	public String getB_subject() {
		return b_subject;
	}

	public String getB_lccn() {
		return b_lccn;
	}

	public String getB_numpage() {
		return b_numpage;
	}

	public String getB_edition() {
		return b_edition;
	}

	public String getB_description() {
		return b_description;
	}

	public int getB_numcopy() {
		return b_numcopy;
	}

	public String getB_rare() {
		return b_rare;
	}

	public String getB_pubdate() {
		return b_pubdate;
	}
	
	public String getB_category() {
		return b_category;
	}
}
