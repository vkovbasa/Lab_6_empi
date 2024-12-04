import java.util.Random;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class LinearRegressionWithNoise extends ApplicationFrame {

    public LinearRegressionWithNoise(String title) {
        super(title);
    }

    public static void main(String[] args) {
        int N = 5; // Номер студента
        int points = N + 10; // Кількість точок
        double k = 2.0; // Нахил
        double b = 1.0; // Зміщення
        double noiseRange = N / 5.0;

        // Генерація даних лінії
        double[] x = new double[points];
        double[] yOriginal = new double[points];
        double[] yNoisy = new double[points];
        Random random = new Random();

        for (int i = 0; i < points; i++) {
            x[i] = i;
            yOriginal[i] = k * x[i] + b;
            yNoisy[i] = yOriginal[i] + random.nextGaussian() * noiseRange;
        }

        // Обчислення коефіцієнтів регресії
        double[] regressionCoefficients = calculateLinearRegression(x, yNoisy);
        double kRegression = regressionCoefficients[0];
        double bRegression = regressionCoefficients[1];

        // Вивід коефіцієнтів
        System.out.println("Коефіцієнти вихідної лінії: k = " + k + ", b = " + b);
        System.out.println("Коефіцієнти регресійної моделі: k = " + kRegression + ", b = " + bRegression);

        // Побудова графіків
        XYSeries originalLine = new XYSeries("Вихідна лінія");
        XYSeries noisyLine = new XYSeries("Зашумлені дані");
        XYSeries regressionLine = new XYSeries("Регресійна модель");

        for (int i = 0; i < points; i++) {
            originalLine.add(x[i], yOriginal[i]);
            noisyLine.add(x[i], yNoisy[i]);
            regressionLine.add(x[i], kRegression * x[i] + bRegression);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(originalLine);
        dataset.addSeries(noisyLine);
        dataset.addSeries(regressionLine);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Лінійна регресія з шумом",
                "X",
                "Y",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        LinearRegressionWithNoise app = new LinearRegressionWithNoise("Лінійна регресія");
        app.setContentPane(panel);
        app.pack();
        app.setVisible(true);
    }

    // Метод для обчислення коефіцієнтів регресії
    private static double[] calculateLinearRegression(double[] x, double[] y) {
        int n = x.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
        }

        double k = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double b = (sumY - k * sumX) / n;

        return new double[]{k, b};
    }
}
