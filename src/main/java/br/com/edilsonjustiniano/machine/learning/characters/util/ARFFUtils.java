package br.com.edilsonjustiniano.machine.learning.characters.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ARFFUtils {

	public static String generateHeader(String relation, String clazz, String... attributes) {

		StringBuilder header = new StringBuilder("@relation ");
		header.append(relation);
		header.append("\n\n");
		
		for (String attr : attributes) {
			header.append("@attribute ");
			header.append(attr);
			header.append(" real\n");
		}

		header.append("@attribute class ");
		header.append(clazz);
		header.append("\n\n");

		return header.toString();
	}

	public static String generateDataBody(String data) {

		StringBuilder body = new StringBuilder("@data\n");

		body.append(data);

		return body.toString();
	}

	public static void generateARRFFile(String filename, String content) throws IOException {

		File arquivo = new File(filename);
		FileOutputStream f = new FileOutputStream(arquivo);
		f.write(content.toString().getBytes());
		f.close();
	}

}
