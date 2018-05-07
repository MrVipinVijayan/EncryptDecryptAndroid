package encrypt_decrypt.coderzheaven.com.encryptdecryptandroid;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import java.io.FileDescriptor;

/**
 * Created by James from CoderzHeaven on 5/7/18.
 */

public class Player implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private Handler handler;

    public Player(Handler handler) {
        this.handler = handler;
    }

    private void initPlayer() {
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
        }
    }

    public void play(FileDescriptor fileDescriptor) {
        initPlayer();
        stopAudio();
        playAudio(fileDescriptor);
    }

    public void playAudio(FileDescriptor fileDescriptor) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setDataSource(fileDescriptor);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
        }
    }

    private void stopAudio() {
        if (null != mediaPlayer && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Message message = new Message();
        message.obj = "Audio play completed.";
        handler.dispatchMessage(message);
    }

    private void releasePlayer() {
        if (null != mediaPlayer) {
            mediaPlayer.release();
        }
    }

    public void destroyPlayer() {
        stopAudio();
        releasePlayer();
        mediaPlayer = null;
    }
}
