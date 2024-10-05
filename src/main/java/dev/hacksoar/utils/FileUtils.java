package dev.hacksoar.utils;

import dev.hacksoar.HackSoar;

import java.io.*;

public class FileUtils {
	
    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            // Don't give crackers clues...
            if (HackSoar.instance.DEVELOPMENT_SWITCH)
                e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    
    public static void createDir(File file) {
    	if(!file.exists()) {
    		file.mkdir();
    	}
    }
    
    public static void createFile(File file) {
    	if(!file.exists()) {
    		try {
				file.createNewFile();
			} catch (IOException e) {
                // Don't give crackers clues...
                if (HackSoar.instance.DEVELOPMENT_SWITCH)
                    e.printStackTrace();
			}
    	}
    }
}
