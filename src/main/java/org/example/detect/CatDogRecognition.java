package org.example.detect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import javax.visrec.ml.model.ModelCreationException;

/**
 * @author Nishant Raut
 *
 */
public class CatDogRecognition {
	public static void main(String[] args) throws IOException, ModelCreationException {

		// Configuration to train the model
		ImageClassifier<BufferedImage> classifier = NeuralNetImageClassifier.builder().inputClass(BufferedImage.class)
																																				.importModel(Paths.get("catdog.dnet"))
																																				.build();











		// Get input image from resources and use the classifier. cat_36 dog_23
		URL input = CatDogRecognition.class.getClassLoader().getResource("cat_36.png");
		if (input == null) {
			throw new IOException("Input file not found");
		}

		BufferedImage image = ImageIO.read(new File(input.getFile()));
		Map<String, Float> results = classifier.classify(image);

		// Print the outcome
		System.out.println(results);










	}
}
