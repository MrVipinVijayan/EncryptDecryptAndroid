package encrypt_decrypt.coderzheaven.com.encryptdecryptandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static encrypt_decrypt.coderzheaven.com.encryptdecryptandroid.Constants.DIR_NAME;
import static encrypt_decrypt.coderzheaven.com.encryptdecryptandroid.Constants.FILE_EXT;
import static encrypt_decrypt.coderzheaven.com.encryptdecryptandroid.Constants.FILE_NAME;
import static encrypt_decrypt.coderzheaven.com.encryptdecryptandroid.Constants.TEMP_FILE_NAME;

/**
 * Created by Vipin on 5/6/18.
 */

public class FileUtils {

    public static void saveFile(byte[] encodedBytes, String path) {
        try {
            File file = new File(path);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(encodedBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] readFile(String filePath) {
        byte[] contents;
        File file = new File(filePath);
        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(
                    new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }

    @NonNull
    public static File createTempFile(Context context, byte[] decrypted) throws IOException {
        File tempFile = File.createTempFile(TEMP_FILE_NAME, FILE_EXT, context.getCacheDir());
        tempFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(decrypted);
        fos.close();
        return tempFile;
    }

    public static FileDescriptor getTempFileDescriptor(Context context, byte[] decrypted) throws IOException {
        File tempFile = FileUtils.createTempFile(context, decrypted);
        FileInputStream fis = new FileInputStream(tempFile);
        return fis.getFD();
    }

    public static final String getDirPath(Context context) {
        return context.getDir(DIR_NAME, Context.MODE_PRIVATE).getAbsolutePath();
    }

    public static final String getFilePath(Context context) {
        return getDirPath(context) + File.separator + FILE_NAME;
    }

    public static final void deleteDownloadedFile(Context context) {
        File file = new File(getFilePath(context));
        if (null != file && file.exists()) {
            if (file.delete()) Log.i("FileUtils", "File Deleted.");
        }
    }


}
