package encrypt_decrypt.coderzheaven.com.encryptdecryptandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;

import java.io.FileDescriptor;
import java.io.IOException;

import static encrypt_decrypt.coderzheaven.com.encryptdecryptandroid.Constants.DOWNLOAD_AUDIO_URL;
import static encrypt_decrypt.coderzheaven.com.encryptdecryptandroid.Constants.FILE_NAME;

public class MainActivity extends AppCompatActivity implements OnDownloadListener, Handler.Callback {

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = new Player(new Handler(this));
    }

    public final void updateUI(String msg) {
        ((TextView) findViewById(R.id.statusTv)).setText(msg);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.download:
                downloadAudio();
                break;
            case R.id.encrypt:
                if (encrypt()) updateUI("File encrypted successfully.");
                break;
            case R.id.decrypt:
                if (null != decrypt()) updateUI("File decrypted successfully.");
                break;
            case R.id.play:
                playClicked();
                break;
            default:
                updateUI("Unknown Click");
        }
    }

    private void playClicked() {
        try {
            playAudio(FileUtils.getTempFileDescriptor(this, decrypt()));
        } catch (IOException e) {
            updateUI("Error Playing Audio.\nException: " + e.getMessage());
            return;
        }
    }

    private void downloadAudio() {
        // Delete the old file //
        FileUtils.deleteDownloadedFile(this);
        updateUI("Downloading audio file...");
        PRDownloader.download(DOWNLOAD_AUDIO_URL, FileUtils.getDirPath(this), FILE_NAME).build().start(this);
    }

    /**
     * Encrypt and save to disk
     *
     * @return
     */
    private boolean encrypt() {
        updateUI("Encrypting file...");
        try {
            byte[] fileData = FileUtils.readFile(FileUtils.getFilePath(this));
            byte[] encodedBytes = EncryptDecryptUtils.encode(EncryptDecryptUtils.getInstance(this).getSecretKey(), fileData);
            FileUtils.saveFile(encodedBytes, FileUtils.getFilePath(this));
            return true;
        } catch (Exception e) {
            updateUI("File Encryption failed.\nException: " + e.getMessage());
        }
        return false;
    }

    /**
     * Decrypt and return the decoded bytes
     *
     * @return
     */
    private byte[] decrypt() {
        updateUI("Decrypting file...");
        try {
            byte[] fileData = FileUtils.readFile(FileUtils.getFilePath(this));
            byte[] decryptedBytes = EncryptDecryptUtils.decode(EncryptDecryptUtils.getInstance(this).getSecretKey(), fileData);
            return decryptedBytes;
        } catch (Exception e) {
            updateUI("File Decryption failed.\nException: " + e.getMessage());
        }
        return null;
    }

    private void playAudio(FileDescriptor fileDescriptor) {
        if (null == fileDescriptor) {
            return;
        }
        updateUI("Playing audio...");
        player.play(fileDescriptor);
    }

    @Override
    public void onDownloadComplete() {
        updateUI("File Download complete");
    }

    @Override
    public void onError(Error error) {
        updateUI("File Download Error");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.destroyPlayer();
    }

    @Override
    public boolean handleMessage(Message message) {
        if (null != message) {
            updateUI(message.obj.toString());
        }
        return false;
    }
}
