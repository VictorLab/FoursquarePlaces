package helper;


import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import victor.co.za.foursquareplaces.R;

/**
 * Created by Victor on 2018/04/20.
 */

public class CustomDialogs {

    /**
     * Custom Snackbar
     * @param activity
     * @param mainTextString
     * @param actionString
     * @param listener
     */
    public static void showSnackBar(Activity activity,final String mainTextString, final String actionString, View.OnClickListener listener) {
        View container = activity.findViewById(R.id.container);
        if (container != null) {
            Snackbar.make(container, mainTextString, Snackbar.LENGTH_INDEFINITE)
                    .setAction(actionString, listener)
                    .show();
        }
    }

}
