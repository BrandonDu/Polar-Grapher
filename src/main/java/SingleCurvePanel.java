import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SingleCurvePanel extends JPanel implements ActionListener {

	private SingleCurveGraph graph;
	private double begTheta, endTheta;
	private String func;
	private JTextField function, theta1Text, theta2Text;
	private JButton startAnimation;

	SingleCurvePanel() {
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		function = new JTextField("Function");
		function.addActionListener(this);
		function.setActionCommand("function");
		this.add(function);

		JPanel bounds = new JPanel();
		theta1Text = new JTextField("Theta 1");
		theta2Text = new JTextField("Theta 2");

		bounds.add(theta1Text);
		bounds.add(theta2Text);
		this.add(bounds);

		ImageIcon start = new ImageIcon("Images/Start Button.png");
		Image image = start.getImage();
		Image newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		start = new ImageIcon(newimg);
		startAnimation = new JButton(start);
		startAnimation.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		startAnimation.addActionListener(this);
		startAnimation.setActionCommand("start");
		this.add(startAnimation);
		
		graph = new SingleCurveGraph();
		this.add(graph);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String eventName = e.getActionCommand();
		switch (eventName) {
		case "function":
			func = function.getText();
			if (func == null)
				break;
			this.remove(graph);
			graph = new SingleCurveGraph(func);
			this.add(graph);
			this.revalidate();
			this.repaint();
			break;
		case "start":
			func = function.getText();
			if (func == null)
				break;
			begTheta = Double.parseDouble(theta1Text.getText());
			endTheta = Double.parseDouble(theta2Text.getText());
			this.remove(graph);
			graph = new SingleCurveGraph(func, begTheta, endTheta);
			this.add(graph);
			this.revalidate();
			this.repaint();
			graph.beginAnimation();
			break;
		}
	}

}

class SingleCurveGraph extends Graph {

	private String func;
	private int dim;
	private double scale, begTheta, endTheta;
	private boolean draw;
	private List<Line2D> lineList;

	SingleCurveGraph() {
		dim = 500;
		scale = 1;
		lineList = new ArrayList<>();
		this.setPreferredSize(new Dimension(dim, dim));
		draw = false;
	}
	
	SingleCurveGraph(String func) {
		this.func = func;
		dim = 500;
		scale = findScale(func);
		lineList = new ArrayList<>();
		this.setPreferredSize(new Dimension(dim, dim));
		draw = true;
	}
	SingleCurveGraph(String func, double begTheta, double endTheta) {
		this.func = func;
		this.begTheta = begTheta;
		this.endTheta = endTheta;
		dim = 500;
		scale = findScale(func);
		lineList = new ArrayList<>();
		this.setPreferredSize(new Dimension(dim, dim));
		draw = true;
	}

	@Override
	public void drawSingleLine(double theta) {
		double r = evaluate(func, theta);
		int x = (int) (scale * r * Math.cos(theta));
		int y = (int) (scale * r * Math.sin(theta));
		Line2D line = new Line2D.Double(dim / 2, dim / 2, dim / 2 + x, dim / 2 - y);
		lineList.add(line);
	}

	@Override
	public void beginAnimation() {
		final Timer timer = new Timer();
		lineList.clear();
		TimerTask task = new TimerTask() {
			double theta = begTheta;

			@Override
			public void run() {
				drawSingleLine(theta);
				repaint();
				theta += 0.001;
				if (theta > endTheta)
					timer.cancel();
			}
		};
		timer.scheduleAtFixedRate(task, 0, 5);
	}
	
	

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHints(hints);

		if (draw) {
			for (double theta = 0; theta < 2 * Math.PI; theta += 0.0001) {
				double r = evaluate(func, theta);
				int x = (int) (scale * r * Math.cos(theta));
				int y = (int) (scale * r * Math.sin(theta));
				g2.drawLine(dim / 2 + x, dim / 2 - y, dim / 2 + x, dim / 2 - y);
			}
		}

		g2.setColor(Color.BLACK);
		g2.drawLine(dim / 2, 0, dim / 2, dim);
		g2.drawLine(0, dim / 2, dim, dim / 2);
		
		g2.setColor(new Color(0, 255, 51, 30));
		Iterator<Line2D> it = lineList.iterator();
		while (it.hasNext()) {
			Line2D line2d = it.next();
			g2.draw(line2d);
		}
	}

}