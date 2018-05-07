package encrypt_decrypt.coderzheaven.com.encryptdecryptandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static encrypt_decrypt.coderzheaven.com.encryptdecryptandroid.Constants.SECRET_KEY;

public class PrefUtils {

    public static final PrefUtils prefUtils = new PrefUtils();
    public static SharedPreferences myPrefs = null;

    public static PrefUtils getInstance(Context context) {
        if (null == myPrefs)
            myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefUtils;
    }

    public void saveSecretKey(String value) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString(SECRET_KEY, value);
        editor.commit();
    }

    public String getSecretKey() {
        return myPrefs.getString(SECRET_KEY, null);
    }
}
