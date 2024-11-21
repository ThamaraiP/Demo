package org.example.multiClass.util;

import deepnetts.data.TabularDataSet;
import deepnetts.util.DeepNettsException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import javax.visrec.ml.data.BasicDataSet;
import javax.visrec.ml.data.Column;
import javax.visrec.ml.data.DataSet;

public class DataSetPreparation {

    public static DataSet getTravelAdvisorDataSet(String name, int inputsNum, int outputsNum) throws IOException {
        return DataSetPreparation.fromURL(DataSetPreparation.class.getClassLoader().getResource(name),
                ",", inputsNum, outputsNum, true);
    }



    private static BasicDataSet fromURL(URL url, String delimiter, int inputsNum, int outputsNum, boolean hasColumnNames) throws IOException {
        TabularDataSet dataSet = new TabularDataSet(inputsNum, outputsNum);

        URLConnection conn = url.openConnection();
        String[] content;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            content = reader.lines().toArray(String[]::new);
        }
        if (content == null) {
            throw new NullPointerException("content == null");
        } else if (content.length <= 1 && hasColumnNames) {
            throw new IllegalArgumentException("content has one line of columns");
        } else if (content.length == 0) {
            throw new IllegalArgumentException("content has no lines");
        }

        int skipCount = 0;
        if (hasColumnNames) {    // get col names from the first line
            String[] colNames = content[0].split(delimiter);
            dataSet.setColumnNames(colNames);
            skipCount = 1;
        } else {
            String[] colNames = new String[inputsNum + outputsNum];
            for (int i = 0; i < inputsNum; i++)
                colNames[i] = "in" + (i + 1);

            for (int j = 0; j < outputsNum; j++)
                colNames[inputsNum + j] = "out" + (j + 1);

            dataSet.setColumnNames(colNames);
        }

        List<Column> cols = dataSet.getColumns();
        for(int i=0; i<outputsNum; i++) {
            //dataSet.getColumns().get(inputsNum+i).setAsTarget(true);          
          cols.get(inputsNum+i).setAsTarget(true);
        }

        Arrays.stream(content)
                .skip(skipCount)
                .filter(l -> !l.isEmpty())
                .map(l -> toBasicDataSetItem(l, delimiter, inputsNum, outputsNum))
                .forEach(dataSet::add);
        return dataSet;
    }


    public static class ExampleDataSet {
        private File labelsFile;
        private File trainingFile;

        private ExampleDataSet() {
        }

        private ExampleDataSet setLabelsFile(File labelsFile) {
            this.labelsFile = labelsFile;
            return this;
        }

        private ExampleDataSet setTrainingFile(File trainingFile) {
            this.trainingFile = trainingFile;
            return this;
        }

        public File getLabelsFile() {
            return labelsFile;
        }

        public File getTrainingFile() {
            return trainingFile;
        }

        @Override
        public String toString() {
            return "ExampleDataSet{" +
                    "labelsFile=" + labelsFile +
                    ", trainingFile=" + trainingFile +
                    '}';
        }
    }


    private static TabularDataSet.Item toBasicDataSetItem(String line, String delimiter, int inputsNum, int outputsNum) {
        String[] values = line.split(delimiter);
        if (values.length != (inputsNum + outputsNum)) {
            throw new DeepNettsException("Wrong number of values found " + values.length + " expected " + (inputsNum + outputsNum));
        }
        float[] in = new float[inputsNum];
        float[] out = new float[outputsNum];

        try {
            // these methods could be extracted into parse float vectors
            for (int i = 0; i < inputsNum; i++) { //parse inputs
                in[i] = Float.parseFloat(values[i]);
            }

            for (int j = 0; j < outputsNum; j++) { // parse outputs
                out[j] = Float.parseFloat(values[inputsNum + j]);
            }
        } catch (NumberFormatException nex) {
            throw new DeepNettsException("Error parsing csv, number expected: " + nex.getMessage(), nex);
        }

        return new TabularDataSet.Item(in, out);
    }

}
