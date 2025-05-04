package examples.zcat.zcatandroidsamples.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;



public class ViewUtils {

    public static void setTextViewColor(Context context, TextView textView, int colorId) {
        textView.setTextColor(ContextCompat.getColor(context, colorId));
    }

    public static int getColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }

    public static boolean isSafeContext(Context context) {
        if (context == null) {
            return false;
        }
        else if (context instanceof FragmentActivity) {
            return !((FragmentActivity) (context)).isDestroyed();
        }
        else if (context instanceof Activity) {
            return !((Activity) (context)).isDestroyed();
        }
        return true;
    }

}
