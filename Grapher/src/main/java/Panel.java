import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Panel extends JPanel {
	private String func1;
	private String func2;
	private int dim;
	private final double scale;

	private List<Line2D> lineList = new ArrayList<>();
	private HashMap<Line2D, Color> map = new HashMap<>();

	Panel(String s1, String s2) {
		func1 = s1;
		func2 = s2;
		dim = 500;
		scale = Math.max(findScale(func1), findScale(func2));

		this.setPreferredSize(new Dimension(dim, dim));
		beginAnimation();
	}

	private void drawSingleLine(double theta) {
		double f1 = evaluate(func1, theta);
		double f2 = evaluate(func2, theta);
		Color color;
		if (f1 > f2) {
			color = Color.GREEN;
		} else
			color = Color.PINK;

		if (f1 < 0 || f2<0)
			return;
		double r = Math.min(f1, f2);

		int x = (int) (scale * r * Math.cos(theta));
		int y = (int) (scale * r * Math.sin(theta));

		Line2D line = new Line2D.Double(dim / 2, dim / 2, dim / 2 + x, dim / 2 - y);

		lineList.add(line);
		map.put(line, color);
	}

	private void beginAnimation() {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			double theta = 0;

			@Override
			public void run() {
				drawSingleLine(theta);
				repaint();
				System.out.println(theta);
				theta += 0.005;
				if (theta > 2 * Math.PI)
					timer.cancel();

			}
		};
		timer.scheduleAtFixedRate(task, 0, 10);
	}

	public double findScale(String s) {
		double max = 0;
		for (double theta = 0; theta < 2 * Math.PI; theta += 0.01) {
			max = Math.max(max, evaluate(s, theta));
		}
		double scale = 3d * dim / (8 * max);
		return scale;
	}

	public double evaluate(String s, double val) {
		Expression expression = new ExpressionBuilder(s).variables("x").build().setVariable("x", val);
		return expression.evaluate();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHints(hints);

		for (double theta = 0; theta < 2 * Math.PI; theta += 0.0001) {
			double r = evaluate(func1, theta);
			int x = (int) (scale * r * Math.cos(theta));
			int y = (int) (scale * r * Math.sin(theta));
			g2.drawLine(dim / 2 + x, dim / 2 - y, dim / 2 + x, dim / 2 - y);

			double r2 = evaluate(func2, theta);
			int x2 = (int) (scale * r2 * Math.cos(theta));
			int y2 = (int) (scale * r2 * Math.sin(theta));
			g2.drawLine(x2 + dim / 2, dim / 2 - y2, x2 + dim / 2, dim / 2 - y2);
		}

		g2.drawLine(dim / 2, 0, dim / 2, dim);
		g2.drawLine(0, dim / 2, dim, dim / 2);

		Iterator<Line2D> it = map.keySet().iterator();

		while (it.hasNext()) {
			Line2D line2d = it.next();
			g2.setColor(map.get(line2d));
			g2.draw(line2d);
		}
	}

}
