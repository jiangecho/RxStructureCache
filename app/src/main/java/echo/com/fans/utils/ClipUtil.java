package echo.com.fans.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

/**
 * Created by jiangecho on 15/4/23.
 */
public class ClipUtil {
    public static void clip(Context context, String lable, String data) {
        if (context == null || data == null || TextUtils.isEmpty(data)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 11) {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (lable == null || TextUtils.isEmpty(lable)) {
                lable = data;
            }
            ClipData clipData = ClipData.newPlainText(lable, data);
            clipboardManager.setPrimaryClip(clipData);
        } else {
            android.text.ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(data);
        }

    }
}
