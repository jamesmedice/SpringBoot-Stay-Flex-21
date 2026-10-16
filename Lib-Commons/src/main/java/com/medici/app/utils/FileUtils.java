package com.medici.app.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import  com.medici.app.constants.*;

public class FileUtils extends org.apache.commons.io.FileUtils {

	public static InputStream getFileAsIOStream(final String fileName) {
		InputStream ioStream = FileUtils.class.getClassLoader().getResourceAsStream(fileName);

		if (ioStream == null)
			throw new IllegalArgumentException(MessageFormat.format(ApplicationConstants.FILE_NOT_FOUND, fileName));

		return ioStream;
	}

	public static File getResourceFile(final String fileName) {
		URL url = FileUtils.class.getClassLoader().getResource(fileName);

		if (url == null)
			throw new IllegalArgumentException(MessageFormat.format(ApplicationConstants.FILE_NOT_FOUND, fileName));

		File file = new File(url.getFile());

		return file;
	}

}
