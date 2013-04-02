package com.github.sirkarpfen.utils;

public class StringUtils {
	
	public static int[] parseIntArray(String inputString) {
		String[] strArray = inputString.split(",");
		int[] returnArray = new int[strArray.length];
		for(int i = 0; i < strArray.length; i++) {
			try {
				returnArray[i] = Integer.parseInt(strArray[i]);
			} catch (NumberFormatException e) {
				System.out.println("Bitte überprüfen sie die entity.properties");
				System.out.println("Es wurde ein fehler beim parsen eines Strings entdeckt.");
				return null;
			}
		}
		return returnArray;
	}
	
}
