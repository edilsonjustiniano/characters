package br.com.edilsonjustiniano.machine.learning.characters.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.edilsonjustiniano.machine.learning.characters.classifier.MachineLearningClassifier;
import br.com.edilsonjustiniano.machine.learning.characters.extractor.CharacteristicExtractor;
import br.com.edilsonjustiniano.machine.learning.characters.view.CharacteristicExtractorView;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class CharacteristicExtractorController {

	private static final String IMAGES_DIRECTORY = "src\\main\\resources\\images\\";
	private static final String ARFF_FILE = "src\\main\\resources\\database\\characters.arff";

	private static final int EXTRACT_IMAGE_CHARACTERISTICS_OPTION = 1;
	private static final int CLASSIFY_IMAGE_OPTION = 2;
	private static final int EXIT = 3;

	private static final Logger log = LoggerFactory.getLogger(CharacteristicExtractorController.class);

	private CharacteristicExtractorView characteristicExtractorView;
	private CharacteristicExtractor characteristicExtractor;
	private Instances instances;
	private MachineLearningClassifier machineLearningClassifier;

	private float[] imageCharacteristic = null;

	public CharacteristicExtractorController() {
		characteristicExtractorView = new CharacteristicExtractorView(this);
		try {
			characteristicExtractor = new CharacteristicExtractor();
			characteristicExtractor.processAllImages();
		} catch (IOException e) {
			log.info("Error to process the images to generate the ARFF file", e);
			characteristicExtractorView.sendMessage(
					"I'm so sorry... but we are not able to continue with our app at this moment.\nPlease contact me in order to fix it as soon as possible");
		}
		machineLearningClassifier = new MachineLearningClassifier();
	}

	public void init() {
		int option = 3;
		do {
			characteristicExtractorView.printMenu();
			option = characteristicExtractorView.getPickedOption();
			handleOption(option);
		} while (option < 1 || option > 3);

		String letsContinue = null;
		do {
			characteristicExtractorView.printContinueMenu();
			letsContinue = characteristicExtractorView.shouldContinue();
			handleContinueOption(letsContinue);
		} while (letsContinue == null || ((letsContinue.charAt(0) != 'n' && letsContinue.charAt(0) != 'N')
				&& (letsContinue.charAt(0) == 'y' && letsContinue.charAt(0) == 'Y')));

		init();
	}

	private void handleContinueOption(String letsContinue) {
		switch (letsContinue.charAt(0)) {
		case 'n':
		case 'N':
			log.info("That is so bad :( Dont't worry we understand");
			System.exit(0);
			break;
		case 'y':
		case 'Y':
			log.info("Let's continue my friend");
			break;

		default:
			log.info("Invalid provided option");
			characteristicExtractorView
					.sendMessage("Sorry, but you didn't provide a correct option, could you fix it?");
			break;
		}
	}

	private void handleOption(int option) {
		switch (option) {
		case EXTRACT_IMAGE_CHARACTERISTICS_OPTION: {
			int fileNumber = 294;
			do {
				characteristicExtractorView.sendMessage("Choose an value from 1 to 294...");
				fileNumber = characteristicExtractorView.getPickedOption();
			} while (fileNumber < 1 || fileNumber > 294);

			getImageCharacteristics(fileNumber);
			characteristicExtractorView.showExtractedCharacteristic(imageCharacteristic);
			break;
		}

		case CLASSIFY_IMAGE_OPTION: {
			try {
				loadWekaBase();
				classifyByNaiveBayes();
				classifyByTreeDecision();
			} catch (Exception e) {
				log.debug("Fail to classify the image", e);
				e.printStackTrace();
			}
			break;
		}
		case EXIT:
			characteristicExtractorView.sendMessage("See you soon... :)");
			System.exit(0);
			break;
		default:
			log.info("Invalid provided option");
			break;
		}
	}

	private void getImageCharacteristics(int fileNumber) {
		File file = null;
		File directory = new File(IMAGES_DIRECTORY);
		File[] files = directory.listFiles();

		if (files.length > 0) {
			file = files[fileNumber - 1];
			log.info("Filename: " + file.getPath());
		}

		try {
			imageCharacteristic = characteristicExtractor.extractCharacteristic(file);

		} catch (IOException e) {
			log.info("Error to extract the characteristics from selected image", e);
			characteristicExtractorView.sendMessage(
					"I'm so sorry... It was not possible to extract the characteristics from the provided image! May you try another one?");
		}

	}

	private void loadWekaBase() throws Exception {
		DataSource dataSource = new DataSource(ARFF_FILE);
		instances = dataSource.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);
	}

	private void classifyByNaiveBayes() throws Exception {
		NaiveBayes naiveBayes = new NaiveBayes();
		System.out.println("Classification by Naive Bayes:\n");
		machineLearningClassifier.classify(naiveBayes, instances, imageCharacteristic);
	}
	
	private void classifyByTreeDecision() throws Exception {
		J48 tree = new J48();
		System.out.println("Classification by Trees:\n");
		machineLearningClassifier.classify(tree, instances, imageCharacteristic);
	}

}
