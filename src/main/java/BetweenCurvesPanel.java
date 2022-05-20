import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class BetweenCurvesPanel extends JPanel implements ActionListener {
	private String func1, func2;
	private JTextField function1, function2;
	private JButton startAnimation;
	private BetweenCurvesGraph graph;
	
	BetweenCurvesPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel input = new JPanel();
		input.setLayout(new GridLayout(1, 2));

		function1 = new JTextField("Function 1");
		function2 = new JTextField("Function 2");
		
		function1.addActionListener(this);
		function1.setActionCommand("f1");
		function2.addActionListener(this);
		function2.setActionCommand("f2");
		
		input.add(function1);
		input.add(function2);

		graph = new BetweenCurvesGraph();

		ImageIcon start = new ImageIcon("Images/Start Button.png");
		
		Image image = start.getImage();
		Image newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		start = new ImageIcon(newimg);
		startAnimation = new JButton(start);
		startAnimation.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		startAnimation.addActionListener(this);
		startAnimation.setActionCommand("start");
		
		this.add(input);
		this.add(startAnimation);
		this.add(graph);
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String eventName = e.getActionCommand();
		switch (eventName) {
		case "f1":
			func1 = function1.getText();
			func2 = function2.getText();
			if (func2 != null) {
				this.remove(graph);
				graph = new BetweenCurvesGraph(func1, func2);
				this.add(graph);
				this.revalidate();
				this.repaint();
			}
			break;
		case "f2":
			func1 = function1.getText();
			func2 = function2.getText();
			if (func1 != null) {
				this.remove(graph);
				graph = new BetweenCurvesGraph(func1, func2);
				this.add(graph);
				this.revalidate();
				this.repaint();
			}
			break;

		case "start":
			func1 = function1.getText();
			func2 = function2.getText();
			this.remove(graph);
			graph = new BetweenCurvesGraph(func1, func2);
			this.add(graph);
			this.revalidate();
			this.repaint();
			graph.beginAnimation();
			break;
		}
	}

}

class BetweenCurvesGraph extends Graph {
	private boolean draw;
	private String func1, func2;
	private double scale;
	private int dim;
	private HashMap<Line2D, Color> map;

	BetweenCurvesGraph() {
		scale = 1;
		dim = 500;
		this.setPreferredSize(new Dimension(dim, dim));
		map = new HashMap<>();
		draw = false;
	}

	BetweenCurvesGraph(String s1, String s2) {
		func1 = s1;
		func2 = s2;
		draw = true;
		dim = 500;
		scale = Math.min(findScale(func1), findScale(func2));
		map = new HashMap<>();
		this.setPreferredSize(new Dimension(dim, dim));
	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHints(hints);

		if (draw) {
			g2.setColor(Color.PINK);
			for (double theta = 0; theta < 2 * Math.PI; theta += 0.0001) {
				double r = evaluate(func1, theta);
				int x = (int) (scale * r * Math.cos(theta));
				int y = (int) (scale * r * Math.sin(theta));
				g2.drawLine(dim / 2 + x, dim / 2 - y, dim / 2 + x, dim / 2 - y);
			}

			g2.setColor(Color.GREEN);
			for (double theta = 0; theta < 2 * Math.PI; theta += 0.0001) {
				double r2 = evaluate(func2, theta);
				int x2 = (int) (scale * r2 * Math.cos(theta));
				int y2 = (int) (scale * r2 * Math.sin(theta));
				g2.drawLine(x2 + dim / 2, dim / 2 - y2, x2 + dim / 2, dim / 2 - y2);
			}
		}
		g2.setColor(Color.BLACK);
		g2.drawLine(dim / 2, 0, dim / 2, dim);
		g2.drawLine(0, dim / 2, dim, dim / 2);

		Iterator<Line2D> it = map.keySet().iterator();
		while (it.hasNext()) {
			Line2D line2d = it.next();
			g2.setColor(map.get(line2d));
			g2.draw(line2d);
		}
	}

	@Override
	public void drawSingleLine(double theta) {
		double f1 = evaluate(func1, theta);
		double f2 = evaluate(func2, theta);

		double f3 = theta > Math.PI ? -evaluate(func1, theta - Math.PI) : -evaluate(func1, theta + Math.PI);
		double f4 = theta > Math.PI ? -evaluate(func2, theta - Math.PI) : -evaluate(func2, theta + Math.PI);

		if (f1 < 0)
			f1 = Math.max(f1, f3);
		else if (f3 > 0)
			f1 = Math.min(f1, f3);

		if (f2 < 0)
			f2 = Math.max(f2, f4);
		else if (f4 > 0)
			f2 = Math.min(f2, f4);

		if (f1 < 0 || f2 < 0)
			return;

		Color color = f1 > f2 ? new Color(0, 255, 51, 80) : new Color(255, 192, 203, 90);

		double r = Math.min(f1, f2);

		int x = (int) (scale * r * Math.cos(theta));
		int y = (int) (scale * r * Math.sin(theta));

		Line2D line = new Line2D.Double(dim / 2, dim / 2, dim / 2 + x, dim / 2 - y);

		map.put(line, color);

	}

	@Override
	public void beginAnimation() {
		final Timer timer = new Timer();
		map.clear();
		TimerTask task = new TimerTask() {
			double theta = 0;

			@Override
			public void run() {
				drawSingleLine(theta);
				repaint();
				theta += 0.005;
				if (theta > 2 * Math.PI)
					timer.cancel();
			}
		};
		timer.scheduleAtFixedRate(task, 0, 5);
	}
}