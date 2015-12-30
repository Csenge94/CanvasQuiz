package edu.ubbcluj.canvas.util.network;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import org.apache.http.cookie.Cookie;

import java.util.List;

import edu.ubbcluj.canvas.persistence.PersistentCookieStore;
import edu.ubbcluj.canvas.persistence.model.SingletonCookie;
import edu.ubbcluj.canvas.view.activity.LoginActivity;

public class CheckSavedSession extends AsyncTask<LoginActivity, Void, Void> {

    private PersistentCookieStore cookieStore;
    private LoginActivity activity;
    private boolean cookieIsAvailable = false;

    /**
     * AsyncTask overridden method.
     */
    @Override
    protected Void doInBackground(LoginActivity... activities) {

        for (LoginActivity activity : activities) {
            this.activity = activity;
            cookieStore = new PersistentCookieStore(activity.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE));

            if (hasLoginCookies(cookieStore.getCookies())) {
                SingletonCookie.getInstance().setCookieStore(cookieStore);
                cookieIsAvailable = true;

                return null;
            }

            return null;
        }

        return null;
    }

    /**
     * AsyncTask overridden method.
     */
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (cookieIsAvailable) {
            activity.loginCompleted();
        } else {
 //           activity.setVisibility(View.VISIBLE);
        }
    }

    private boolean hasLoginCookies(List<Cookie> cookies) {

        for (Cookie c : cookies) {
            if ((c.getName().startsWith("_normandy_session"))
                    || (c.getName().startsWith("pseudonym_credentials"))) {
                return true;
            }
        }

        return false;
    }


}
