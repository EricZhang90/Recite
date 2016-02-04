package ClientSide;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 
 * @author eric
 * Note window where user can see all vocabulary he/she added
 * 
 */

public class VBook extends JFrame {
	private java.awt.List vocabularyList = new java.awt.List();
	private JButton clearNote = new JButton("Clear Note");
	private Set<String> notes = new HashSet<String>();
	private String username;
	
	/**
	 * 
	 * @param username current user
	 */
	public VBook(String username) {
		this.username = username;
	}
	
	/**
	 * GUI initialization and added functionality to buttons
	 */
	public void init()
	{
		setSize(300, 400);
		setTitle("Note");

		getContentPane().add(clearNote, BorderLayout.SOUTH);
		getContentPane().add(vocabularyList, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
		// delete all vocabulary
		clearNote.addActionListener(e ->{
			vocabularyList.removeAll();
			notes.clear();
			java.util.List<String> empty = Arrays.asList("");
			try {
				Files.write(Paths.get(username + ".dat"), empty);
			} catch (Exception e1) {
				System.err.println("Fail to clear Note");
			}
		});
		
		// rewrite all vocabulary in order to delete duplicate vocabulary
		// user pressed "add to note" many time, as the result, 
		// there were duplicate words in the local file where vocabulary
		// stored.
		// the name of file that stored words is current username plus .dat extension
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				String writeFilePath = username + ".dat";
				try {
					Files.write(Paths.get(writeFilePath),notes);
				}catch(IOException e1){
					System.err.println("Cannot rewrite notes");
				}
			}
		});
	}
	
	/**
	 * inilized GUI and read words
	 */
	public void start()
	{
		init();
		try {
			readWords();
		} catch (IOException e) {
			System.err.println("File reading error\n" + e);
		}
	}
	
	/**
	 * loaded words from local file and added them to set.
	 * then display them in JList
	 * @throws IOException
	 */
	public void readWords() throws IOException
	{
		String readFilePath = username + ".dat";
		java.util.List<String> lines = Files.readAllLines(Paths.get(readFilePath), Charset.forName("utf-8"));
		for(String s : lines)
			notes.add(s);
		
		for(String s: notes)
			vocabularyList.add(s);
	}
}
