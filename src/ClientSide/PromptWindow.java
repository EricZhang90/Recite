package ClientSide;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PromptWindow extends JFrame{
	JLabel lbMsg = new JLabel("");
	JButton comfirm = new JButton("Ok");
	
	public PromptWindow(String msg) 
	{
		this.lbMsg.setText(msg);
		setTitle("Message");
		setSize(300, 100);

		JPanel button = new JPanel( new FlowLayout(FlowLayout.CENTER));
		button.add(comfirm);
		JPanel center = new JPanel( new FlowLayout(FlowLayout.CENTER));
		center.add(lbMsg);
		add(center, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		comfirm.addActionListener(e->dispose());
	}
	
	public static void main(String[] args) {
		new PromptWindow("abc");
	}
	
}
