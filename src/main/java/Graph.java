
import javax.swing.JPanel;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public abstract class Graph extends JPanel {
	private int dim = 500;
	
	public double evaluate(String s, double val) {
		Expression expression = new ExpressionBuilder(s).variables("x").build().setVariable("x", val);
		return expression.evaluate();
	}

	public double findScale(String s) {
		double max = 0;
		for (double theta = 0; theta < 2 * Math.PI; theta += 0.01) {
			max = Math.max(max, Math.abs(evaluate(s, theta)));
		}
		double scale = 7d * dim / (16 * max);
		System.out.println(scale);
		return scale;
	}

	public abstract void drawSingleLine(double theta);

	public abstract void beginAnimation();
}
