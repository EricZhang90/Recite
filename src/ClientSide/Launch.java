package ClientSide;

import javax.swing.SwingUtilities;

/**
 * 
 * @author Eric
 * 
 * @version 1.0 
 * 2016/01/03
 * 1, read file by thread
 * 2, offer 4 choices
 * 3, set limited time to answer question
 * 4, date in title
 * 5, record score
 * 
 * @version 1.1.0
 * 2016/01/04
 * 1, add a new class VBook (vocabulary book) so that user is able to customer own vocabulary book
 * 2, data of VBook is wrote in Vocabulary.dat
 * 3, automatically check duplicate words and duplicate words wont be display 
 * 
 * @version 1.1.1
 * 2016/01/04
 * 1, use borderLayout with direction in VBook instead of manual setting component's position
 * 
 * @version 1.1.2
 * 2016/01/05
 * 1, add windows listener to delete duplicate vocabulary in data file when note is closing
 * 2, add SWingInvokeLater to all functions that are in thread and change GUI
 * 3, fix the bug that user cannot clear note
 * 
 * @version 1.1.3
 * 2016/01/12
 * 1, refactor all codes
 * 2, fix a bug that "add to note" adds previous vocabulary, which happens after I refactored all codes.
 * 
 * @version 1.2.0
 * 2016/01/19
 * 
 * 1, add two databases:
 * 
 * user profile:
 * create table userProfile( 
 * username char (20) primary key, 
 * password char (40) not null, 
 * hint char (30));
 * 
 * user's daily accomplishment: 
 * create table accomplishment( 
 * username char (20), 
 * updateDate date, 
 * updateTime mediumint (6) unsigned not null,
 * correct mediumint (4) unsigned not null, 
 * incorrect mediumint (4) unsigned not null, 
 * currentIndex mediumint (4) unsigned not null,
 * primary key (username, updateDate, updateTime),  
 * FOREIGN KEY (username) references userprofile (username));
 * 
 * 2, add a class DBManager to manager database
 * 3, add a class LoginWindow which creates a login window and validate user information by connecting database
 * 
 * @version 1.2.1
 * 2016/01/21
 * 1, add a class Statistics showing correct rate and practise date
 * 2, delete Reset button
 * 3, update DBMananger so that DBMananger is able to retrieval and receive mutiple messages.
 * 4, user progress is automatically stored into database when the application closing.
 *  
 *  @version 1.2.2
 *  2016/01/22
 *  1, add server side, move all functions who need to connect to dabsebase to server side. 
 *  1, refactor those functions to seperate classes
 *  
 *  @version 1.3
 *  2016/01/24
 *  1, revise all classes, seperate them to client side and server side
 */

public class Launch {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->new MyRecite().start());
	}
}
