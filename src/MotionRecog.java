import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

/**
 * Created by jayvee on 15/10/6.
 */
public class MotionRecog {
    public static void main(String a[]) {
        System.out.printf("test");
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);
        DataSet trainSet = new DataSet(2, 1);
        trainSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        trainSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{1}));
        neuralNetwork.learn(trainSet);
        neuralNetwork.save("or_perceptron.nnet");

    }
}
