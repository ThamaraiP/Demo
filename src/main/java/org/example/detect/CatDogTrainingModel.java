package org.example.detect;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import javax.visrec.ml.model.ModelCreationException;

/**
 * @author Nishant Raut
 *
 */
public class CatDogTrainingModel {
	public static void main(String[] args) throws IOException, ModelCreationException {

		// Configuration to train the model
		ImageClassifier<BufferedImage> classifier = NeuralNetImageClassifier.builder().inputClass(BufferedImage.class)
																																				.imageHeight(128).imageWidth(128)
																																				.labelsFile(Paths.get("src/main/resources/dataset/cats_and_dogs/training/labels.txt"))
																																				.trainingFile(Paths.get("src/main/resources/dataset/cats_and_dogs/training/train.txt"))
																																				.networkArchitecture(Paths.get("src/main/resources/catdog_arch.json"))
																																				.exportModel(Paths.get("catdog.dnet"))
																																				.maxError(0.03f)
																																				.maxEpochs(1000)
																																				.learningRate(0.01f).build();


	}
}
