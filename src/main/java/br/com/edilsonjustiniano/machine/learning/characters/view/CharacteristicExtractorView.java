package br.com.edilsonjustiniano.machine.learning.characters.view;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.edilsonjustiniano.machine.learning.characters.controller.CharacteristicExtractorController;

public class CharacteristicExtractorView {

	private static final Logger log = LoggerFactory.getLogger(CharacteristicExtractorView.class);

	Scanner input = new Scanner(System.in);

	private CharacteristicExtractorController characteristicExtractorController;

	public CharacteristicExtractorView(CharacteristicExtractorController characteristicExtractorController) {
		this.characteristicExtractorController = characteristicExtractorController;
	}

	public void printMenu() {

		System.out.println("Hi there!!! Let's start our first ML app\n\n");

		System.out.println("Please pick one of the options below:");
		System.out.println("1 - Extract image's characteristics");
		System.out.println("2 - Classify selected image");
		System.out.println("3 - Exit\n\n");

		System.out.println("Make yourself comfortable to choose any of them... We are waiting for you......\n");
	}

	public int getPickedOption() throws NumberFormatException {
		int pickedOption = 0;

		try {
			pickedOption = Integer.parseInt(input.nextLine());
		} catch (NumberFormatException e) {
			log.info("Invalid provided option");
		}

		return pickedOption;
	}

	public void printContinueMenu() {
		System.out.println("Let's continue? If so, press y(Y), otherwise press n(N)");

		System.out.println("We are waiting for your response... go ahead and have fun.....");
	}

	public String shouldContinue() {
		return input.nextLine();
	}

	public void sendMessage(String message) {
		System.out.println(message);
	}

	public void showExtractedCharacteristic(float[] imageCharacteristic) {
		StringBuilder extractedCharacteristicMessage = new StringBuilder(
				"Hey there! Let's talk about the characteristics: \n");
		extractedCharacteristicMessage.append("Bart's Orange t-shirt: ");
		extractedCharacteristicMessage.append(imageCharacteristic[0]);
		extractedCharacteristicMessage.append("\t");

		extractedCharacteristicMessage.append("Homer's Brown beard : ");
		extractedCharacteristicMessage.append(imageCharacteristic[3]);
		extractedCharacteristicMessage.append("\n");

		extractedCharacteristicMessage.append("Bart's Blue pants: ");
		extractedCharacteristicMessage.append(imageCharacteristic[1]);
		extractedCharacteristicMessage.append("\t");

		extractedCharacteristicMessage.append("Homer's Blue pants : ");
		extractedCharacteristicMessage.append(imageCharacteristic[4]);
		extractedCharacteristicMessage.append("\n");

		extractedCharacteristicMessage.append("Bart's Blue shoes: ");
		extractedCharacteristicMessage.append(imageCharacteristic[2]);
		extractedCharacteristicMessage.append("\t");

		extractedCharacteristicMessage.append("Homer's Grey shoes : ");
		extractedCharacteristicMessage.append(imageCharacteristic[5]);
		extractedCharacteristicMessage.append("\n");

		System.out.println(extractedCharacteristicMessage.toString());
	}
}
