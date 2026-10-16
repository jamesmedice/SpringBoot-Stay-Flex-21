package com.medici.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Encoder;

public class ConvertCertificateToBase64EncodedStringUtils {

    public static String convert(final File file) throws Exception {
        try (InputStream inputStream = new FileInputStream(file)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[65535];
            int numberOfReadBytes;

            while ((numberOfReadBytes = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, numberOfReadBytes);
            }

            byte[] certificateInBytes = byteArrayOutputStream.toByteArray();

            Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode(certificateInBytes);
            return new String(encodedBytes);
        }
    }

}