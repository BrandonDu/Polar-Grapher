import javax.swing.JFrame;
import javax.swing.JPanel;

public class Grapher {
	private JFrame frame;
	private JPanel panel;
	private String func1;
	private String func2;

	Grapher(String s1, String s2) {
		func1 = s1;
		func2 = s2;

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new Panel(func1, func2);
		frame.setContentPane(panel);
		
		frame.setVisible(true);
		frame.pack();
	}

	public static void main(String[] args) {
		Grapher graph = new Grapher("cos(x^2)", "6+2cos(x)");
	}

}
