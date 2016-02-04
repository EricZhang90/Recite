package ClientSide;

import javax.swing.SwingUtilities;

// there was version description, I moved them to ReadMe 
public class Launch {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->new MyRecite().start());
	}
}
