package edu.ubbcluj.canvas.controller.canvasAPI;

import android.content.Context;
import android.widget.Toast;

import com.instructure.canvasapi.model.CanvasError;
import com.instructure.canvasapi.utilities.APIStatusDelegate;
import com.instructure.canvasapi.utilities.CanvasCallback;

import retrofit.RetrofitError;

public class CanvasErrorDelegate implements com.instructure.canvasapi.utilities.ErrorDelegate, APIStatusDelegate {

    @Override
    public void noNetworkError(RetrofitError error, Context context) {
        Toast.makeText(context, "No network", Toast.LENGTH_LONG).show();
    }

    @Override
    public void notAuthorizedError(RetrofitError error, CanvasError canvasError, Context context) {
        Toast.makeText(context, "Not authorized", Toast.LENGTH_LONG).show();
    }

    @Override
    public void invalidUrlError(RetrofitError error, Context context) {
        Toast.makeText(context, "Invalid url", Toast.LENGTH_LONG).show();
    }

    @Override
    public void serverError(RetrofitError error, Context context) {
        Toast.makeText(context, "Server error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void generalError(RetrofitError error, CanvasError canvasError, Context context) {
        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCallbackStarted() {

    }

    @Override
    public void onCallbackFinished(CanvasCallback.SOURCE source) {

    }

    @Override
    public void onNoNetwork() {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
