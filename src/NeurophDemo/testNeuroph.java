package NeurophDemo;


import Utils.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.norm.Normalizer;

import java.util.Arrays;

/**
 * Created by jayvee on 15/10/7.
 */
public class testNeuroph {
    public static void main(String args[]) {
        MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(4,  3);
        neuralNetwork.addLayer(0, new Layer(4, new NeuronProperties()));
//        DataSet trainSet = DataSet.createFromFile("/Users/jayvee/github_project/jobs/senz.analyzer.motion.java/src/sample/data/iris_data_normalised.txt",
//                4, 3, ",", false);
        DataSet trainSet = new DataSet(4, 3);
        String s = FileUtils.File2str("/Users/jayvee/github_project/jobs/senz.analyzer.motion.java/src/sample/data/iris_data_normalised_small.txt", "utf-8");
        String[] lines = s.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] ch = line.split(",");
            trainSet.addRow(new DataSetRow(new double[]{Double.valueOf(ch[0]),
                    Double.valueOf(ch[1]),
                    Double.valueOf(ch[2]),
                    Double.valueOf(ch[3])},
                    new double[]{Double.valueOf(ch[4]), Double.valueOf(ch[5]), Double.valueOf(ch[6])}));
        }
//        Normalizer norm = new MaxMinNormalizer();
//        norm.normalize(trainSet);

////        trainSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
//        trainSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
//        trainSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
//        trainSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{1}));
        System.out.printf("train");
//        neuralNetwork.setNetworkType(NeuralNetworkType.BOLTZMAN);
//        neuralNetwork.addListener(new NeuralNetworkEventListener() {
//            @Override
//            public void handleNeuralNetworkEvent(NeuralNetworkEvent neuralNetworkEvent) {
//                System.out.printf(neuralNetworkEvent.getEventType() + "\n");
//            }
//        });
        neuralNetwork.learn(trainSet);
        System.out.println("save");
        neuralNetwork.save("or_perceptron.nnet");
        NeuralNetwork testwork = MultiLayerPerceptron.createFromFile("or_perceptron.nnet");
        testwork.setInput(0.1, 0.4, 0.3, 0.2);
        testwork.calculate();
        double[] output = testwork.getOutput();
        System.out.printf(Arrays.toString(output));
    }


}
