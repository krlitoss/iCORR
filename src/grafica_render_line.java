/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRChart;
import org.jfree.chart.JFreeChart;
import java.awt.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
/**
 *
 * @author IVONNE
 */
public class grafica_render_line implements JRChartCustomizer{
    protected static final java.awt.Font ITEM_FONT = new java.awt.Font("Arial", Font.PLAIN, 6);

	public void customize(final JFreeChart chart, final JRChart jasperChart){
        CategoryPlot plot = chart.getCategoryPlot();
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelFont(ITEM_FONT);
        renderer.setItemLabelsVisible(true);
        renderer.setItemLabelGenerator( new StandardCategoryItemLabelGenerator() );
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.12);
	}

}
