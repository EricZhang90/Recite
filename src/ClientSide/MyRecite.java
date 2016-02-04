package ClientSide;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * 
 * @author eric
 * main function
 * created main GUI, loaded data file and provided entry for sub-windows
 * set a test
 * made connection if necessary
 */
public class MyRecite extends JFrame{
	private static final int TOTAL_SECOND = 20;  
	private JLabel labelTimer = new JLabel("Time");
	private AtomicInteger currentSec = new AtomicInteger(TOTAL_SECOND);
	
	// word
	private JLabel labelWord = new JLabel("  Word");
	private ArrayList<String> wordList = new ArrayList<>();
	
	//score
	private int right = 0;
	private int wrong = 0;
	private JLabel score = new JLabel("  Correct: " + right + " / Wrong: " + wrong);
	
	//options
	private JButton optionA = new JButton("A");
	private JButton optionB = new JButton("B");
	private JButton optionC = new JButton("C");
	private JButton optionD = new JButton("D");
	private int index = 0;
	private ArrayList<String> meaningList = new ArrayList<>();
	private String[] questionList = new String[4];

	//note
	private JButton addToNote = new JButton("Add To Note");
	private JButton openNote = new JButton("Open Note");
	
	//Client:
	protected Client client;
	protected String username = null;
	private JButton login = new JButton("Login");
	private JButton btnToLastIndex = new JButton("To last index");
	private JButton btnStatistic = new JButton("Statistic");
	
	/**
	 * GUI initialization and created buttons' functionality, 
	 * 
	 */
	public void GUIInit()
	{
		setSize(600, 250);
		setLayout(new GridLayout(7, 2));
		changeTitle();
		getContentPane().add(labelWord);
		getContentPane().add(addToNote);
		getContentPane().add(labelTimer);
		getContentPane().add(openNote);
		getContentPane().add(score);
		getContentPane().add(btnStatistic);
		//getContentPane().add(reset);
		add(login);
		getContentPane().add(btnToLastIndex);
		getContentPane().add(new JLabel(""));
		getContentPane().add(new JLabel(""));
		getContentPane().add(optionA);
		getContentPane().add(optionB);
		getContentPane().add(optionC);
		getContentPane().add(optionD);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		//updated user progress while uesr exiting
		addWindowListener( 
				new WindowAdapter(){
					@Override
					public void windowClosing(WindowEvent e) 
					{ 
						if(username != null)
							updateProgress();
					}
				});
		
		//add current word in to client's note
		addToNote.addActionListener(e->{
			String writeFilePath = username + ".dat";
			try {
				String note = wordList.get(index-1) + "\t" + meaningList.get(index-1);
				Files.write(Paths.get(writeFilePath), Arrays.asList(note), StandardOpenOption.APPEND);
			} catch (Exception e1) {
				System.err.println("File Writing error\n" + e);
			}
		});
		
		//open client's note
		openNote.addActionListener(e ->new VBook(this.username).start());
		
		
		// four funtions below were used confirm user's choice
		optionA.addActionListener(e ->check(optionA.getText(), index));
		
		optionB.addActionListener(e ->check(optionB.getText(), index));
		
		optionC.addActionListener(e ->check(optionC.getText(), index));
		
		optionD.addActionListener(e ->check(optionD.getText(), index));
		
		// call function login
		login.addActionListener(e->{
			if(username == null){
				login();
			}
			});
		
		// skiped to the last index user looked
		btnToLastIndex.addActionListener(e->{
			client.getCurrentIndex(username);
			try {
				int idx = Integer.parseInt(client.getBufferedReader().readLine());
				if(idx != index){
					index = idx;
					currentSec.set(0);
					 this.wrong--;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		// open statistic window
		btnStatistic.addActionListener(e->new Thread(){public void run() {new Statistics(client);}}.start());
		
		// functions, including open note, Add word to note, return to the last word user looked and open statisitc window,
		// are not available before user successfully long in.
		openNote.setEnabled(false);
		addToNote.setEnabled(false);
		btnToLastIndex.setEnabled(false);
		btnStatistic.setEnabled(false);
	}

	/**
	 * checked wether or not user is logged in
	 * if yes, do nothing.
	 * if not, make conntion with server and open login window
	 * meanwhile, started threading to listen feedback from server.
	 * if the message is "T", which meant user login information is valid, close login widow then enable rest funtions
	 * otherwise, do nothing but close login window
	 */
	private void login() 
	{
		if(this.username == null){
			client = new Client();
			LoginWindow login = new LoginWindow(client);
			
			Thread check = new Thread(()->{
				if (login != null){
					try {
						String result = client.getBufferedReader().readLine();
						if(result.equals("T")){
							username = login.getUsername();
							login.close();
							btnStatistic.setEnabled(true);
							btnToLastIndex.setEnabled(true);
							addToNote.setEnabled(true);
							openNote.setEnabled(true);
						}
						if(result.equals("F")){
							login.close();
							new PromptWindow("Incorrect username or password");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			
			check.start();
		}
	}
	
	/**
	 * started threading to read local data file,
	 * statred timer reset it to 20 if it goes down to zero,
	 * user has 20 second to choose a answer
	 */
	public void start()
	{
		GUIInit();
		new Thread(()->{
			try{
				readWords("college.dat", "GB2312");
			}catch(IOException e){
				e.printStackTrace();
			}
			
			if(index == 0)
				SwingUtilities.invokeLater(()->setQuestion());
				
			new Timer(1000, e->{
				SwingUtilities.invokeLater(()->changeTime(currentSec.decrementAndGet()));
				changeTitle();
				
				if(currentSec.intValue() == 0){
					check(null, 1);
					currentSec.set(TOTAL_SECOND);
				}
			}).start();
		}).start();
	}
	
	/**
	 * randomly chose three meaning, and combine the correct answer into four potions
	 * randomly plase them.
	 */
	public void setQuestion()
	{
		
		questionList[0] = meaningList.get(index);
		
		for(int i = 0; i < 3; i++)
			questionList[i+1] = 
				meaningList.get(
						(int) (Math.random()* meaningList.size())
				);
		
		labelWord.setText("  "+ (index+1) + ":" + wordList.get(index));
		
		int []order = {-1, -1, -1, -1};
		
		for(int i = 0; i < 4; i++){
			boolean unique = true;
			int randomNum = (int)(Math.random()*4);
			for(int num : order)
				if( num == randomNum)
					unique = false;
			
			if(unique)
				order[i] = randomNum;
			else
				i--;
		}
		
		optionA.setText(questionList[order[0]]);
		optionB.setText(questionList[order[1]]);
		optionC.setText(questionList[order[2]]);
		optionD.setText(questionList[order[3]]);
		index++;
	}
	
	
	/**
	 * checked if user selected correct answer, increated variable right if so, otherwise increated variable wrong 
	 * @param answer the answer user selcted
	 * @param index the index pointing to correct meaning
	 */
	public void check(String answer, int index)
	{
		if(meaningList.get(index-1).equals(answer))
			this.right++;
		else 
			this.wrong++;
		
		currentSec.set(TOTAL_SECOND);
		this.score.setText("  Correct: " + right + " Wrong: " + wrong);
		setQuestion();
		changeTime(TOTAL_SECOND);
	}
	
	/**
	 * display current time in title
	 */
	public void changeTitle()
	{
		LocalDateTime currentTime = LocalDateTime.now();
		setTitle("Recite " + currentTime.getHour() + 
				':' + currentTime.getMinute() + 
				':' + currentTime.getSecond() + 
				' ' + LocalDate.now()
				);
	}
	
	/**
	 * 
	 * @param i the second to which label current second was changed
	 */
	public void changeTime(int i)
	{
		this.labelTimer.setText("  Time remain: " + i +"sec(s)");
	}
	
	/**
	 * loaded vocaburay
	 * 
	 * @param fileName location of the data file containing vocabulary
	 * @param charset  type of Unicode
	 * @throws IOException 
	 */
	public void readWords(String fileName, String charset) throws IOException
	{
		try(BufferedReader reader =
				new BufferedReader(
						new InputStreamReader(
								new FileInputStream(fileName), charset
		));){
		
			String line;
			while ((line = reader.readLine()) != null){
				if(line.length() == 0) 
					continue;
	
				String[] tokens = Pattern.compile("\t").split(line);
				wordList.add(tokens[0]);
				meaningList.add(tokens[1]);
			}
		}
	}
	
	/**
	 * 	update user current progress,
	 *  the information containing current date, time and index
	 *  set current date and time to string type
	 */
	public void updateProgress()
	{
			LocalDateTime defaulTime = LocalDateTime.now();
			String updateDate = defaulTime.toString().substring(0, 10);
			int hour = defaulTime.getHour();
			int min = defaulTime.getMinute();
			int sec = defaulTime.getSecond();

			String sHour = Integer.toString(hour);
			String sMin = min < 10 ? "0" + Integer.toString(min) : Integer.toString(min);
			String sSec = sec < 10 ? "0" + Integer.toString(sec) : Integer.toString(sec);
			
			String updateTime = sHour + sMin + sSec;
			int correct = this.right;
			int incorrect = this.wrong;
			int index = this.index;
			
			String query = "insert into accomplishment values('" + username + "', '" + updateDate +
						   "', '" + updateTime + "', " + correct + ", " + incorrect
						   + ", " + index + ");";
			client = new Client();
			client.update(query);
	}
	
}
