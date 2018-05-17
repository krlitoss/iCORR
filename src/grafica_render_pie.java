/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRChart;
import org.jfree.chart.JFreeChart;
import java.awt.*;
import java.text.DecimalFormat;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.Rotation;
/**
 *
 * @author ANGEL
 */

public class grafica_render_pie implements JRChartCustomizer{
    protected static final java.awt.Font ITEM_FONT = new java.awt.Font("Arial", Font.PLAIN, 6);
    DecimalFormat normal=new DecimalFormat("###,###,###,###");
    DecimalFormat porcen=new DecimalFormat("##0.0 %");

	public void customize(final JFreeChart chart, final JRChart jasperChart){
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={2}", normal,porcen));
        plot.setLabelFont(ITEM_FONT);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.8f);
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);
	}

}
