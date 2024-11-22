package org.example.multiClass;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.visrec.ml.classification.MultiClassClassifier;
import javax.visrec.ml.data.DataSet;
import javax.visrec.ri.ml.classification.MultiClassClassifierNetwork;
import org.example.multiClass.util.DataSetPreparation;

public class TravelClassificationExample {

  public static final float FAMILY = 2f;
  public static final float ADVENTURE = 1f;
  public static final float JAN_MONTH = 1f;

  public static void main(String[] args) throws IOException {

    int inputsNum = 3;
    int outputsNum = 3;
    DataSet dataSet = DataSetPreparation.getTravelAdvisorDataSet("TravelAdvisor.csv", inputsNum, outputsNum);

    // Build multi class classifier using Deep Netts implementation of Feed Forward Network under the hood
    MultiClassClassifier<float[], String> travelClassifier = MultiClassClassifierNetwork.builder()
                                                                                      .inputsNum(inputsNum)
                                                                                      .hiddenLayers(inputsNum * inputsNum)
                                                                                      .outputsNum(outputsNum)
                                                                                      .maxEpochs(1000)
                                                                                      .maxError(0.003f)
                                                                                      .learningRate(0.00001f)
                                                                                      .trainingSet(dataSet)
                                                                                      .build();

    // Use classifier to predict class - returns a map with probabilities associated to possible classes
   Map<String, Float> placesWithProbabilities = travelClassifier.classify(new float[]{FAMILY,
       ADVENTURE, JAN_MONTH});
    System.out.println(placesWithProbabilities);
  }
}
