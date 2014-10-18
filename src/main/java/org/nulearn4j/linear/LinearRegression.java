package org.nulearn4j.linear;

import org.nulearn4j.dataset.loader.DatasetLoader;
import org.nulearn4j.dataset.matrix.Matrix;
import org.nulearn4j.dataset.matrix.Row;
import org.nulearn4j.dataset.preprocessing.normalization.Normalization;
import org.nulearn4j.dataset.preprocessing.normalization.ZeroMeanUnitVar;
import org.nulearn4j.dataset.validation.Validation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiachiliu on 10/17/14.
 */
public class LinearRegression {

    private double[] weights;
    private double learning_rate;
    private double converged;
    private double maxIteration;

    public LinearRegression(double learning_rate, double converged, double maxIteration) {
        this.learning_rate = learning_rate;
        this.converged = converged;
        this.maxIteration = maxIteration;
    }

    public LinearRegression() {
        learning_rate = 0.0001;
        converged = 0.001;
        maxIteration = 230;
    }

    public LinearRegression fit(Matrix<Double> train, List<Double> target) {
        int[] dimension = train.getDimension();
        int m = dimension[0];
        int n = dimension[1];
        weights = new double[n];
        int k = 1;
        double mse = Validation.mse(predict(train), target);
        System.out.println("============Start fit==============");
        System.out.println("Initialized  weights: " + Arrays.toString(weights) + " mse: " + mse);
        while (k <= maxIteration) {
            for (int i = 0; i < m; i++) {
                Row<Double> row = train.getRow(i);
                Double t = target.get(i);
                double error = predict(row) - t;
                for (int j = 0; j < n; j++) {
                    weights[j] -= learning_rate * error * row.get(j);
                }
            }

            double update = Validation.mse(predict(train), target);
            if (Math.abs(update - mse) <= converged) {
                System.out.println("Converged at Iteration " + k + " weights: " + Arrays.toString(weights) + " mse: " + update);
                break;
            }
            mse = update;
            System.out.println("Iteration " + k + " weights: " + Arrays.toString(weights) + " mse: " + mse);
            k++;
        }

        return this;
    }

    public List<Double> predict(Matrix<Double> test) {
        return test.getMatrix()
                .stream()
                .map(this::predict)
                .collect(Collectors.toList());
    }

    public Double predict(Row<Double> row) {
        double weightedSum = 0;
        for (int i = 0; i < row.size(); i++) {
            weightedSum += weights[i] * row.get(i);
        }
        return weightedSum;
    }

    public static void main(String[] args) throws Exception {

        Matrix<Double> train = DatasetLoader.loadBostonHousingTrain("\\s+");
        int label = train.getColumnCount() - 1;
        List<Double> trainTarget = train.getColumn(label);
        train = train.removeColumn(label);
        Normalization<Double> normalization = new ZeroMeanUnitVar();
        normalization.setUpMeanAndVariance(train);
        normalization.normalize(train);
        train = train.addColumn(0, 1.0);

        Matrix<Double> test = DatasetLoader.loadBostonHousingTest("\\s+");
        List<Double> testTarget = test.getColumn(label);
        test = test.removeColumn(label);
        normalization.normalize(test);
        test = test.addColumn(0, 1.0);

        LinearRegression classifier = new LinearRegression(0.0001, 0.001, 500);
        classifier.fit(train, trainTarget);
        List<Double> predicts = classifier.predict(test);
        double mse = Validation.mse(predicts, testTarget);
        System.out.println("Test MSE: " + mse);


    }
}
