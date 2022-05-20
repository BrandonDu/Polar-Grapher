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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Grapher implements ActionListener {
	private JFrame frame;
	private JPanel contentPane;
	JTextField func1TF;
	JTextField theta1TF;
	JTextField theta2TF;
	JLabel theta1Label;
	JLabel theta2Label;
	JLabel func1;
	Component firstFill;
	Component secondFill;
	boolean single;
	Graph graph;

	Grapher() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		single = true;
		contentPane = new JPanel();
		contentPane.add(Box.createRigidArea(new Dimension(500, 10)));

		contentPane.setPreferredSize(new Dimension(500, 600));
		contentPane.setLayout(new FlowLayout() {
			public Dimension preferredLayoutSize(Container target) {
				Dimension sd = super.preferredLayoutSize(target);
				sd.width = 500;
				return sd;
			}
		});
		contentPane.add(Box.createRigidArea(new Dimension(500, 10)));
		JLabel title = new JLabel();
		ImageIcon img = new ImageIcon("Images/Screen_Shot_2022-05-14_at_6.01.23_PM-removebg-preview.png");
		Image image = img.getImage();
		Image newimg = image.getScaledInstance(500, 125, java.awt.Image.SCALE_SMOOTH);
		img = new ImageIcon(newimg);
		title.setIcon(img);
		contentPane.add(title);

		contentPane.add(Box.createRigidArea(new Dimension(75, 0)));
		contentPane.add(Box.createRigidArea(new Dimension(500, 10)));

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
		contentPane.add(box);

		contentPane.add(Box.createRigidArea(new Dimension(100, 0)));
		contentPane.add(Box.createRigidArea(new Dimension(500, 20)));
		contentPane.add(Box.createRigidArea(new Dimension(500, 10)));

		func1 = new JLabel("r(x) = ");
		func1.setFont(new Font("Serif", Font.BOLD, 27));
		contentPane.add(func1);

		func1TF = new JTextField();
		func1TF.setPreferredSize(new Dimension(100, 30));
		func1TF.setMaximumSize(func1TF.getPreferredSize());
		contentPane.add(func1TF);
		firstFill = Box.createRigidArea(new Dimension(270, 0));
		contentPane.add(firstFill);

//		contentPane.add(Box.createRigidArea(new Dimension(500,10)));

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

		contentPane.add(theta1Label);
		contentPane.add(theta1TF);
		contentPane.add(theta2Label);
		contentPane.add(theta2TF);

		secondFill = Box.createRigidArea(new Dimension(130, 0));
		contentPane.add(secondFill);
		contentPane.add(Box.createRigidArea(new Dimension(500, 40)));
		contentPane.add(Box.createRigidArea(new Dimension(500, 10)));
		contentPane.add(Box.createRigidArea(new Dimension(500, 10)));

		JButton plot = new JButton("Plot");
		plot.setPreferredSize(new Dimension(400, 50));
		contentPane.add(plot);
		plot.setActionCommand("plot");
		plot.addActionListener(this);

		frame.setContentPane(contentPane);
		frame.setVisible(true);
		frame.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		switch (s) {
		case "single":
			func1.setText("r(x) = ");
			firstFill.setPreferredSize(new Dimension(270, 0));
			theta1Label.setText("\u03F4" + subscript("1") + " =");
			theta1Label.setFont(new Font("Serif", Font.BOLD, 25));
			theta2Label.setVisible(true);
			theta2TF.setVisible(true);
			secondFill.setPreferredSize(new Dimension(130, 0));
			contentPane.repaint();
			break;
		case "double":
			single = false;
			func1.setText("r" + subscript("1") + "(x) = ");
			firstFill.setPreferredSize(new Dimension(260, 0));
			theta1Label.setText("r" + subscript("2") + "(x) = ");
			theta1Label.setFont(new Font("Serif", Font.BOLD, 27));
			theta2Label.setVisible(false);
			theta2TF.setVisible(false);
			secondFill.setPreferredSize(new Dimension(260, 0));
			contentPane.repaint();
			break;
		case "plot":
			JPanel panel = new JPanel();

			JPanel top = new JPanel();
			JButton animate = new JButton("animate");
			animate.setPreferredSize(new Dimension(200, 40));
			animate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					graph.beginAnimation();
				}
			});

			JButton restart = new JButton("restart");
			restart.setPreferredSize(new Dimension(200, 40));
			top.add(animate);
			top.add(Box.createRigidArea(new Dimension(20, 0)));
			top.add(restart);
			panel.add(top);

			if (single) {
				String func = func1TF.getText();
				double theta1 = Double.parseDouble(theta1TF.getText());
				double theta2 = Double.parseDouble(theta2TF.getText());
				graph = new SingleCurveGraph(func, theta1, theta2);
				panel.add(graph);
			} else {
				String func1 = func1TF.getText();
				String func2 = theta1TF.getText();
				graph = new BetweenCurvesGraph(func1, func2);
				panel.add(graph);

			}

			JButton back = new JButton("back");
			back.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					frame.setContentPane(contentPane);
					frame.revalidate();
					frame.repaint();
				}

			});
			panel.add(Box.createRigidArea(new Dimension(400, 0)));
			panel.add(back);

			frame.setContentPane(panel);
			frame.revalidate();
			frame.repaint();

		}
	}

	public static String subscript(String str) {
		if (str.equals("1"))
			return "₁";
		return "₂";
	}

	public static void main(String[] args) {
		Grapher grapher = new Grapher();
	}

}
