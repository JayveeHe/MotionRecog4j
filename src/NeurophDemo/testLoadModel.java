package NeurophDemo;

import Utils.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jayvee on 15/10/10.
 */
public class testLoadModel {
    public static MultiLayerPerceptron LoadModelByPickleFile(String pkl_path) {
        String pkl_content = FileUtils.File2str(pkl_path, "utf-8");
        JSONTokener tokener = new JSONTokener(pkl_content);
        JSONObject root = (JSONObject) tokener.nextValue();
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(350, 128, 2);
        Layer hiddenLayer = mlp.getLayerAt(1);
        Layer outputLayer = mlp.getLayerAt(2);
//        MultiLayerPerceptron mlp = new MultiLayerPerceptron(350,2);
//        Layer inputLayer = new Layer(350);
//        Layer hiddenLayer = new Layer(128);
//        Layer outputLayer = new Layer(2);
        JSONArray input2hidden_weights = root.getJSONArray("input2hidden_weights");
        JSONArray hidden_weights = root.getJSONArray("hidden_weights");
        JSONArray hidden2output_weights = root.getJSONArray("hidden2output_weights");
        JSONArray output_weights = root.getJSONArray("output_weights");
        // setup input layer
//        BiasNeuron origin_input_bias = (BiasNeuron) inputLayer.getNeuronAt(input2hidden_weights.length());
//        BiasNeuron origin_hidden_bias = (BiasNeuron) hiddenLayer.getNeuronAt(hidden_weights.length());
//        BiasNeuron origin_output_bias = (BiasNeuron) outputLayer.getNeuronAt(output_weights.length());
//        inputLayer.removeNeuron(origin_input_bias);
        // setup hidden layer
//        hiddenLayer.removeNeuron(origin_hidden_bias);
//        outputLayer.removeNeuron(origin_output_bias);


        // ==========================
        // setup input2hidden weights
        for (int hidden_index = 0; hidden_index < hidden_weights.length(); hidden_index++) {
            Neuron hidden_neuron = hiddenLayer.getNeuronAt(hidden_index);
            Connection[] inputConnections = hidden_neuron.getInputConnections();
            for (int input_index = 0; input_index < input2hidden_weights.length(); input_index++) {
                Connection inputConnection = inputConnections[input_index];
                inputConnection.setWeight(new Weight(input2hidden_weights.getJSONArray(input_index).getDouble(hidden_index)));
            }
            // set bias
            Connection inputBiasConnection = inputConnections[hidden_weights.length()];
            inputBiasConnection.setWeight(new Weight((Double) hidden_weights.get(hidden_index)));
        }
        // setup hidden2output weights
        for (int output_index = 0; output_index < output_weights.length(); output_index++) {
            Neuron output_neuron = outputLayer.getNeuronAt(output_index);
            Connection[] hiddenConnections = output_neuron.getInputConnections();
            for (int hidden_index = 0; hidden_index < hidden_weights.length(); hidden_index++) {
                Connection hiddenConnection = hiddenConnections[hidden_index];
                hiddenConnection.setWeight(new Weight(hidden2output_weights.getJSONArray(hidden_index).getDouble(output_index)));
            }
            // set bias
            Connection hiddenBiasConnection = hiddenConnections[output_weights.length()];
            hiddenBiasConnection.setWeight(new Weight((Double) output_weights.get(output_index)));
        }


        //+++++++++++++++++++++++++

//        for (int i = 0; i < hidden_weights.length(); i++) {
//            Neuron hidden_neuron = hiddenLayer.getNeuronAt(i);
//            // add connection
//            for (int j = 0; j < input2hidden_weights.length(); j++) {
////                hidden_neuron.addInputConnection(inputLayer.getNeuronAt(j), input2hidden_weights.getJSONArray(j).getDouble(i));
//                Connection connection = hidden_neuron.getInputConnections()[j];
//                connection.setFromNeuron(inputLayer.getNeuronAt(j));
//                connection.setWeight(new Weight(input2hidden_weights.getJSONArray(j).getDouble(i)));
////                hidden_neuron.addInputConnection(connection);
//            }
//            // add bias
//
////            hiddenLayer.addNeuron(hidden_weights.length(), new BiasNeuron());
////            hidden_neuron.addInputConnection(new BiasNeuron(), hidden_weights.getDouble(i));
//            Connection connection = hidden_neuron.getInputConnections()[input2hidden_weights.length()];
//            connection.setFromNeuron(new BiasNeuron());
//            connection.setWeight(new Weight((Double) hidden_weights.get(i)));
////            hiddenLayer.setNeuron(i, hidden_neuron);
//        }
//        //setup output layer
//        for (int i = 0; i < output_weights.length(); i++) {
//            Neuron output_neuron = outputLayer.getNeuronAt(i);
//            // add connection
//            for (int j = 0; j < hidden2output_weights.length(); j++) {
////                output_neuron.addInputConnection(hiddenLayer.getNeuronAt(j), hidden2output_weights.getJSONArray(i).getDouble(i));
//                Connection connection = output_neuron.getInputConnections()[j];
//                connection.setFromNeuron(inputLayer.getNeuronAt(j));
//                connection.setWeight(new Weight(input2hidden_weights.getJSONArray(j).getDouble(i)));
////
//
//            }
//            // add bias
////            outputLayer.removeNeuronAt(output_weights.length());
////            output_neuron.addInputConnection(new BiasNeuron(), output_weights.getDouble(i));
//            Connection connection = output_neuron.getInputConnections()[hidden2output_weights.length()];
//            connection.setFromNeuron(new BiasNeuron());
//            connection.setWeight(new Weight((Double) output_weights.get(i)));
//        }

        return mlp;
    }

    public static void main(String args[]) {
        MultiLayerPerceptron mlp = LoadModelByPickleFile("/Users/jayvee/github_project/jobs/senz.analyzer.motion.java/data/params.json");
        String input = FileUtils.File2str("/Users/jayvee/github_project/jobs/senz.analyzer.motion.java/data/test_walking1_fea_data.csv", "utf-8");
        String[] lines = input.split("\n");
        int count = 0;
        ArrayList<double[]> time_nodes = new ArrayList<>();

//        double[] inputs = new double[350];
        for (String line : lines) {
            String[] params = line.split(",");
            double[] line_params = new double[7];
            int inline_count = 0;
            for (String p : params) {
                line_params[inline_count++] = Double.valueOf(p);
            }
            time_nodes.add(line_params);
        }

        int stride = 20;
        int window = 50;
        int dataMaxCount = lines.length;
        int maxDataBlock = (dataMaxCount - window) / stride;
        for (int i = 0; i < maxDataBlock; i++) {
            List<double[]> block = time_nodes.subList(i * stride, i * stride + window);
            double[] inputs = new double[7 * window];
            int index = 0;
            for (double[] a : block) {
                for (double b : a) {
                    inputs[index++] = b;
                }
            }

            mlp.setInput(inputs);
            mlp.calculate();
            double[] output = mlp.getOutput();
            int flag = output[0] > output[1] ? 0 : 1;
            System.out.println(Arrays.toString(output) + "====" + flag);
        }


    }

}
