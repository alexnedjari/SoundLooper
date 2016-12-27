package com.soundlooper.system.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.soundlooper.exception.SoundLooperRuntimeException;

public class FileUtil {
	public static File getSystemTempFolder() {
		String systemTempFolderPath = System.getProperty("java.io.tmpdir");
		File systemTempFolder = new File(systemTempFolderPath);
		if (!systemTempFolder.exists()) {
			systemTempFolder.mkdir();
		}
		return systemTempFolder;
	}

	public static File getTempFile(String prefix, String extention) throws IOException {
		File systemTempFolder = getSystemTempFolder();
		int increment = 0;
		File file = new File(systemTempFolder.getAbsolutePath() + File.separator
				+ getFileName(prefix, extention, increment));
		while (file.exists()) {
			increment++;
			file = new File(systemTempFolder.getAbsolutePath() + File.separator
					+ getFileName(prefix, extention, increment));
			if (increment > 1000) {
				throw new SoundLooperRuntimeException("Unable to create temp file : " + file.getAbsolutePath());
			}
		}
		file.createNewFile();
		return file;
	}

	private static String getFileName(String prefix, String extention, int increment) {
		String fileName = prefix + "_" + new Date().getTime() + "_" + increment + "_" + extention;
		return fileName;
	}
}
