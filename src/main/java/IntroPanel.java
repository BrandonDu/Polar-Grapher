import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class IntroPanel extends JPanel implements ActionListener {
	JTextField theta1TF;
	JTextField theta2TF;
	JLabel theta1Label;
	JLabel theta2Label;
	JLabel func1;
	Component firstFill;
	Component secondFill;
	IntroPanel() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(new FlowLayout() {
			public Dimension preferredLayoutSize(Container target) {
				Dimension sd = super.preferredLayoutSize(target);
				sd.width = 500;
				return sd;
			}
		});
		this.add(Box.createRigidArea(new Dimension(500,10)));
		JLabel title = new JLabel();
		ImageIcon img = new ImageIcon("Images/Screen_Shot_2022-05-14_at_6.01.23_PM-removebg-preview.png");
		Image image = img.getImage();
		Image newimg = image.getScaledInstance(500, 125, java.awt.Image.SCALE_SMOOTH);
		img = new ImageIcon(newimg);
		title.setIcon(img);
		this.add(title);
		this.add(Box.createRigidArea(new Dimension(75, 0)));
		Box box = Box.createVerticalBox();

		JRadioButton singleButton = new JRadioButton(
				"Area From \u03F4" + subscript("1") + " to " + "\u03F4" + subscript("2"));
		singleButton.setFont(new Font("Serif", Font.BOLD, 20));
		singleButton.setSelected(true);
		singleButton.addActionListener(this);
		singleButton.setActionCommand("single");

		JRadioButton doubleButton = new JRadioButton("Area Between Two Curves");
		doubleButton.addActionListener(this);
		doubleButton.setFont(new Font("Serif", Font.BOLD, 20));
		doubleButton.setActionCommand("double");

		ButtonGroup group = new ButtonGroup();
		group.add(singleButton);
		group.add(doubleButton);

		box.add(singleButton);
		box.add(doubleButton);
		this.add(box);

		this.add(Box.createRigidArea(new Dimension(100, 0)));
		this.add(Box.createRigidArea(new Dimension(500, 20)));
		func1 = new JLabel("r(x) = ");
		func1.setFont(new Font("Serif", Font.BOLD, 27));
		this.add(func1);

		JTextField func1TF = new JTextField();
		func1TF.setPreferredSize(new Dimension(100, 30));
		func1TF.setMaximumSize(func1TF.getPreferredSize());
		this.add(func1TF);
		firstFill = Box.createRigidArea(new Dimension(270, 0));
		this.add(firstFill);

		theta1Label = new JLabel("\u03F4" + subscript("1") + " =");
		theta2Label = new JLabel("\u03F4" + subscript("2") + " =");
		theta1Label.setFont(new Font("Serif", Font.BOLD, 25));
		theta2Label.setFont(new Font("Serif", Font.BOLD, 25));

		theta1TF = new JTextField();
		theta2TF = new JTextField();

		theta1TF.setPreferredSize(new Dimension(100, 30));
		theta2TF.setPreferredSize(new Dimension(100, 30));

		theta1TF.setMaximumSize(theta1TF.getPreferredSize());
		theta2TF.setMaximumSize(theta2TF.getPreferredSize());

		this.add(theta1Label);
		this.add(theta1TF);
		this.add(theta2Label);
		this.add(theta2TF);
		
		secondFill = Box.createRigidArea(new Dimension(130,0));
		this.add(secondFill);
		this.add(Box.createRigidArea(new Dimension(500, 40)));
		JButton plot = new JButton("Plot");
		plot.setPreferredSize(new Dimension(400, 50));
		this.add(plot);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		System.out.println(s);
		switch (s) {
		case "single":
			System.out.println("here");
			func1.setText("r(x) = ");
			firstFill.setPreferredSize(new Dimension(270,0));
			theta1Label.setText("\u03F4" + subscript("1") + " =");
			theta1Label.setFont(new Font("Serif", Font.BOLD, 25));
			theta2Label.setVisible(true);
			theta2TF.setVisible(true);
			secondFill.setPreferredSize(new Dimension(130,0));
			repaint();
			break;
		case "double":
			func1.setText("r" + subscript("1") + "(x) = ");
			firstFill.setPreferredSize(new Dimension(260,0));
			theta1Label.setText("r" + subscript("2") + "(x) = ");
			theta1Label.setFont(new Font("Serif", Font.BOLD, 27));
			theta2Label.setVisible(false);
			theta2TF.setVisible(false);
			secondFill.setPreferredSize(new Dimension(260,0));
			repaint();
			break;
		}

	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("RadioButtonDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new IntroPanel();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static String subscript(String str) {
		if(str.equals("1"))
			return "₁";
		return "₂";
	}
}