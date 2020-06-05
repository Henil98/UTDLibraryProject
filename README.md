# UTDLibraryProject
 
Now that you have designed the database for a library system, your next task is to write a scaled-back library program.  Ideally, your program would have table-maintenance dialogs for every actual data table other than those linked to the book itself.  Since that isn’t practical, create a screen that lets you add, change, and delete books.  The purpose of this assignment is for you to learn how to interface to a database from within a program.

Add, change, and delete can work with a limited set of tables, which you have designed in the previous assignment.  Use your design.  For example, you will need to handle multiple copies of a book, possibly in multiple formats.   Stored procedures will help greatly.

Always maintain referential integrity both at the database level and at the level of your program; that is, only allow delete of a secondary table item if there are no references to it.  Here is a sample of how the screen could look, although you should have considerably more information.  Obviously, the title can be quite long.  The list of authors should be separated by semicolons so that your program can use the split() function to separate them.  The publisher should be a drop-down.  In a real system, there would be an “Add” button next to this so you can add a new publisher.

The Save button saves the new book to your database.  You cannot have two books with the same ISBN, so catch that as an error.  When you save a book, put the information in the list shown.  

The Search button takes any of the shown fields except Publisher and searches for books that match any non-null field.  You can use the “like” operator in SQL to find partial matches on authors and titles, but ISBN and Dewey Decimal numbers must be exact.  Show all matches in the list.  If a user clicks an item in the list, fill in the entry fields and go to change mode to update book information.

The Clear button clears all fields, including the list, and returns to “Add” mode.

The status bar at the bottom can be used to show the mode as well as completed actions. 
