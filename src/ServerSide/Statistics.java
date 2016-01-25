package ServerSide;

import java.awt.BorderLayout;
import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import ClientSide.Client;

public class Statistics extends JFrame{
	DefaultListModel<String> dialog = new DefaultListModel<>();
	Client client;
	
	public Statistics(Client client)
	{
		init();
		this.client = client;
		client.getProgress();
		getData();
	}
	
	public void init()
	{
		setTitle("Statistics");
		setSize(300, 300);
		add(new JScrollPane(new JList<String>(dialog) ), BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void getData()
	{
		BufferedReader in = client.getBufferedReader();
		String data = null;
		try {
			while((data = in.readLine()) != null){
				String a = data;
				SwingUtilities.invokeLater(()->dialog.addElement(a));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Statistics(new Client());
	}
}
