package com.jmgarzo.tools;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jmgarzo on 18/03/15.
 */
public class ToolsFile {


    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    public static boolean borrarFichero(String url) {

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        boolean result = false;
        String state = Environment.getExternalStorageState();

        // COMPROBACION DEL ALMACENAMIENTO EXTERNO
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Podremos leer y escribir en ella
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // En este caso solo podremos leer los datos
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // No podremos leer ni escribir en ella
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (mExternalStorageWriteable) {

            File fBorrar = new File(url);
            result = fBorrar.delete();
        }

        return result;
    }
}