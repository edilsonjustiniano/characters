package br.com.edilsonjustiniano.machine.learning.characters.classifier;

import java.text.DecimalFormat;

import weka.classifiers.AbstractClassifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class MachineLearningClassifier {

	public double[] classify(AbstractClassifier classifier, Instances instances, float[] characteristics) throws Exception {

		// Build the table of probabilities
		classifier.buildClassifier(instances);

		// create a new instance (register)
		Instance register = new DenseInstance(instances.numAttributes());

		// set the reference to the database loaded previously
		register.setDataset(instances);

		for (int i = 0; i < 6; i++) {
			register.setValue(i, characteristics[i]);
		}

		DecimalFormat df = new DecimalFormat("#,###.0000");
		double result[] = classifier.distributionForInstance(register);
		System.out.println("Bart: " + df.format(result[0]) + " Homer: " + df.format(result[1]));

		return result;
	}
}
