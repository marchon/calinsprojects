package ro.ranking.reporting;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PRChart {
	private JFreeChart chart;

	private XYDataset createDataSets(PRChartInfo info) {
		final XYSeriesCollection collection = new XYSeriesCollection();

		for (Entry<String, double[]> precs : info.getPinterps().entrySet()) {
			final XYSeries series = new XYSeries(precs.getKey());

			double[] pr = precs.getValue();

			for (int i = 0; i < pr.length; i++) {
				series.add((double) i / 10.0, pr[i]);
			}

			collection.addSeries(series);
		}

		return collection;
	}

	public PRChart(PRChartInfo info) {
		final XYDataset data = createDataSets(info);
		final XYItemRenderer renderer = new XYSplineRenderer();
		final NumberAxis precAxis = new NumberAxis("Interpolated Precision");
		final NumberAxis recallAxis = new NumberAxis("Recall");
		final XYPlot plot = new XYPlot(data, recallAxis, precAxis, renderer);

		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setOrientation(PlotOrientation.VERTICAL);

		chart = new JFreeChart(info.getCorpus(), JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
	}

	public void writeToPNGFile(OutputStream os) throws IOException {
		BufferedImage bi = chart.createBufferedImage(1024, 768);
        EncoderUtil.writeBufferedImage(bi, ImageFormat.PNG, os);
	}
}
