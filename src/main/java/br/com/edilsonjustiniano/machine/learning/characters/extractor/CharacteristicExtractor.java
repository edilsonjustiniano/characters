package br.com.edilsonjustiniano.machine.learning.characters.extractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.edilsonjustiniano.machine.learning.characters.util.ARFFUtils;

public class CharacteristicExtractor {

	private static final String HOMER = "Homer";
	private static final String BART = "Bart";
	private static final String RESOURCES_DIRECTORY = "src\\main\\resources\\";
	private static final String DATABASE_ARFF_DIRECTORY = "src\\main\\resources\\database\\";
	private static final String IMAGES_DIRECTORY = RESOURCES_DIRECTORY + "images\\";
	private static final int NUMBER_OF_IMAGES = 293;

	private static final Logger LOGGER = LoggerFactory.getLogger(CharacteristicExtractor.class);

	public void processAllImages() throws IOException {
		String attributes[] = new String[] { "bart_orange_tshirt", "bart_blue_pants", "bart_blue_shoes",
				"homer_brown_beard", "homer_blue_pants", "homer_grey_shoes" };

		String header = ARFFUtils.generateHeader("characteristics", "{Bart, Homer}", attributes);
		StringBuilder data = new StringBuilder();

		File directory = new File(IMAGES_DIRECTORY);
		File[] files = directory.listFiles();

		// Define the characteristics' matrix
		float[][] characteristics = new float[NUMBER_OF_IMAGES][attributes.length + 1];

		for (int i = 0; i < files.length; i++) {
			// Define the class: Homer or Bart
			int characterClass = defineCharacterClass(files[i]);

			float[] extractedCharacteristic = extractCharacteristic(files[i]);

			characteristics[i][0] = extractedCharacteristic[0];
			characteristics[i][1] = extractedCharacteristic[1];
			characteristics[i][2] = extractedCharacteristic[2];
			characteristics[i][3] = extractedCharacteristic[3];
			characteristics[i][4] = extractedCharacteristic[4];
			characteristics[i][5] = extractedCharacteristic[5];
			characteristics[i][6] = characterClass;

			LOGGER.info(String.format(
					"Bart's orange T-shrit: %f - " + "Bart's blue pants: %f - " + "Bart's blue shoes: %f - "
							+ "Homer's brown beard: %f - " + "Homer's blue pants: %f - " + "Homer's grey shoes: %f - "
							+ "Class: %f",
					characteristics[i][0], characteristics[i][1], characteristics[i][2], characteristics[i][3],
					characteristics[i][4], characteristics[i][5], characteristics[i][6]));

			fillCharacteristic(data, characteristics, i, 0);
			fillCharacteristic(data, characteristics, i, 1);
			fillCharacteristic(data, characteristics, i, 2);
			fillCharacteristic(data, characteristics, i, 3);
			fillCharacteristic(data, characteristics, i, 4);
			fillCharacteristic(data, characteristics, i, 5);
			data.append(characterClass == 0 ? BART : HOMER);
			data.append("\n");
		}

		String body = ARFFUtils.generateDataBody(data.toString());
		ARFFUtils.generateARRFFile(DATABASE_ARFF_DIRECTORY + "characters.arff", new String(header + body));
	}

	public float[] extractCharacteristic(File file) throws IOException {
		int red, green, blue;
		float bartOrangeTshirt = 0;
		float bartBluePants = 0;
		float bartBlueShoes = 0;
		float homerBrownBeard = 0;
		float homerBluePants = 0;
		float homerGreyShoes = 0;

		// Define the characteristics' vector
		float[] characteristics = new float[6];

		BufferedImage image = ImageIO.read(new File(file.getPath()));
		int width = image.getWidth();
		int height = image.getHeight();

		for (int horizontal = 0; horizontal < height; horizontal++) {
			for (int vertical = 0; vertical < width; vertical++) {
				int pixel = image.getRGB(vertical, horizontal);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = pixel & 0xff;

				// Bart's orange T-shirt
				if (isBartOrangeTshirt(red, green, blue)) {
					bartOrangeTshirt++;
				}

				// Bart's blue pants (bottom of the image)
				if (isBartBluePants(red, green, blue, image, horizontal)) {
					bartBluePants++;
				}

				// Bart's blue shoes (bottom of the image)
				if (isBartBlueShoes(red, green, blue, image, horizontal)) {
					bartBlueShoes++;
				}

				// Homer's brown beard (middle of the image)
				if (isHomerBrownBeard(red, green, blue, image, horizontal)) {
					homerBrownBeard++;
				}

				// Homer's blue pants
				if (isHomerBluePants(red, green, blue, image, horizontal)) {
					homerBluePants++;
				}

				// Homer's grey shoes (bottom of the image)
				if (isHomerGreyShoes(red, green, blue, image, horizontal)) {
					homerGreyShoes++;
				}
			}
		}

		// Normalize the characteristics according the number of pixels in the image
		bartOrangeTshirt = normalizeCharacteristicValue(bartOrangeTshirt, image);
		bartBluePants = normalizeCharacteristicValue(bartBluePants, image);
		bartBlueShoes = normalizeCharacteristicValue(bartBlueShoes, image);
		homerBrownBeard = normalizeCharacteristicValue(homerBrownBeard, image);
		homerBluePants = normalizeCharacteristicValue(homerBluePants, image);
		homerGreyShoes = normalizeCharacteristicValue(homerGreyShoes, image);

		characteristics[0] = bartOrangeTshirt;
		characteristics[1] = bartBluePants;
		characteristics[2] = bartBlueShoes;
		characteristics[3] = homerBrownBeard;
		characteristics[4] = homerBluePants;
		characteristics[5] = homerGreyShoes;

		LOGGER.info(String.format(
				"Bart's orange T-shrit: %f - " + "Bart's blue pants: %f - " + "Bart's blue shoes: %f - "
						+ "Homer's brown beard: %f - " + "Homer's blue pants: %f - " + "Homer's grey shoes: %f",
				characteristics[0], characteristics[1], characteristics[2], characteristics[3], characteristics[4],
				characteristics[5]));

		return characteristics;
	}

	private float normalizeCharacteristicValue(float chacteristc, BufferedImage image) {
		return (chacteristc / (image.getHeight() * image.getWidth())) * 100;
	}

	private void fillCharacteristic(StringBuilder data, float[][] characteristics, int line, int column) {
		data.append(characteristics[line][column]);
		data.append(",");
	}

	private int defineCharacterClass(File file) {
		int characterClass;

		if ('b' == file.getName().charAt(0)) {
			characterClass = 0;
		} else {
			characterClass = 1;
		}

		return characterClass;
	}

	private boolean isHomerGreyShoes(double red, double green, double blue, BufferedImage imagem, int h) {
		if (h > (imagem.getHeight() / 2) + (imagem.getHeight() / 3)) {
			if (blue >= 25 && blue <= 45 && green >= 25 && green <= 45 && red >= 25 && red <= 45) {
				return true;
			}
		}

		return false;
	}

	private boolean isHomerBrownBeard(double red, double green, double blue, BufferedImage imagem, int h) {
		if (h < (imagem.getHeight() / 2) + (imagem.getHeight() / 3)) {
			if (blue >= 95 && blue <= 140 && green >= 160 && green <= 185 && red >= 175 && red <= 207) {
				return true;
			}
		}

		return false;
	}

	private boolean isHomerBluePants(double red, double green, double blue, BufferedImage imagem, int h) {
		if (h > (imagem.getHeight() / 2)) {
			if (blue >= 150 && blue <= 180 && green >= 98 && green <= 120 && red >= 0 && red <= 90) {
				return true;
			}
		}

		return false;
	}

	private boolean isBartBlueShoes(double red, double green, double blue, BufferedImage imagem, int h) {
		if (h > (imagem.getHeight() / 2) + (imagem.getHeight() / 3)) {
			if (blue >= 125 && blue <= 140 && green >= 5 && green <= 12 && red >= 0 && red <= 20) {
				return true;
			}
		}

		return false;
	}

	private boolean isBartBluePants(double red, double green, double blue, BufferedImage imagem, int h) {
		if (h > (imagem.getHeight() / 2)) {
			if (blue >= 125 && blue <= 170 && green >= 0 && green <= 12 && red >= 0 && red <= 20) {
				return true;
			}
		}

		return false;
	}

	private boolean isBartOrangeTshirt(double red, double green, double blue) {
		if (blue >= 11 && blue <= 22 && green >= 85 && green <= 105 && red >= 240 && red <= 255) {
			return true;
		}

		return false;
	}
}
