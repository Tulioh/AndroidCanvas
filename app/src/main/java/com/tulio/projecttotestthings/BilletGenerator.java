package com.tulio.projecttotestthings;

import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by tulio on 2/18/16.
 */
public class BilletGenerator {

    public File generatePDFFromBase64(String base64) throws IOException {
        File tempFolder = Environment.getExternalStorageDirectory();
        final String fileName = String.valueOf(new Date().getTime());
        final String fileExtension =  ".pdf";
        File pdfFile = File.createTempFile(fileName, fileExtension, tempFolder);

        byte[] pdfAsBytes = Base64.decode(base64, 0);
        FileOutputStream os;
        os = new FileOutputStream(pdfFile, false);
        os.write(pdfAsBytes);
        os.flush();
        os.close();
        return pdfFile;
    }
}
