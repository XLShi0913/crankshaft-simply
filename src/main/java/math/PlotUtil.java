package math;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import javax.swing.*;

/**
 * 抄的，画折线图
 * https://blog.51cto.com/acevi/2048520
 */
public class PlotUtil{
    public static void plotXY(double[][] data, String title){
        CategoryDataset dataset = createDataset(data);
        JFreeChart chart = createChart(dataset, title);
        ChartPanel chartf = new ChartPanel(chart,true);
        JFrame jf = new JFrame();
        jf.add(chartf);
        jf.setVisible(true);
        jf.setSize(1400, 600);
        jf.setLocationRelativeTo(null);
    }

    private static CategoryDataset createDataset(double[][] data) {
        return DatasetUtilities.createCategoryDataset("", "", data);
    }

    private static JFreeChart createChart(CategoryDataset categoryDataset, String title){
        // 创建JFreeChart对象：ChartFactory.createLineChart
        JFreeChart jfreechart = ChartFactory.createLineChart(title, // 标题
                "x",         //categoryAxisLabel （category轴，横轴，X轴标签）
                "y",      // valueAxisLabel（value轴，纵轴，Y轴的标签）
                categoryDataset,  //Dataset
                PlotOrientation.VERTICAL, false, // legend
                false,          //Tooltips
                false);        //URLs

        // 使用CategoryPlot设置各种参数。
        CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();

        // 背景色 透明度
        plot.setBackgroundAlpha(0.5f);

        // 前景色 透明度
        plot.setForegroundAlpha(1.0f);

        // 其他设置 参考 CategoryPlot类
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
        renderer.setBaseShapesVisible(true); // series 点（即数据点）可见
        renderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
        renderer.setUseSeriesOffset(true); // 设置偏移量
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(false);
        return jfreechart;
    }

}