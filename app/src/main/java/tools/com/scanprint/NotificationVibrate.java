package tools.com.scanprint;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class NotificationVibrate {

    private Context context;
    private AudioManager audioManager;
    private Vibrator vibrator;

    public void init(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void notificationVibrate() {
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            notification();
            vibrate();
        } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            vibrate();
        } else {

        }
    }

    private void notification() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context.getApplicationContext(), uri);
        rt.play();
    }

    private void vibrate() {
        vibrator.vibrate(new long[]{0, 500},-1);
    }

}
