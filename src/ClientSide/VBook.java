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

public class VBook extends JFrame {
	private java.awt.List vocabularyList = new java.awt.List();
	private JButton clearNote = new JButton("Clear Note");
	private Set<String> notes = new HashSet<String>();
	private String username;
	
	public VBook(String username) {
		this.username = username;
	}
	
	public void init()
	{
		setSize(300, 400);
		setTitle("Note");

		getContentPane().add(clearNote, BorderLayout.SOUTH);
		getContentPane().add(vocabularyList, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
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
	
	public void start()
	{
		init();
		try {
			readWords();
		} catch (IOException e) {
			System.err.println("File reading error\n" + e);
		}
	}
	
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
