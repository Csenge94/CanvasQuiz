package edu.ubbcluj.canvasAndroid.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity; 
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.instructure.canvasapi.utilities.APIHelpers;

import java.util.Arrays;

import edu.ubbcluj.canvasAndroid.R;

public class OAuthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_PROGRESS);

        WebView webview = new WebView(this);
        setContentView(webview);


        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                String code = getUrlParameter(url,"code");
                Log.d(APIHelpers.LOG_TAG,"code: "+ code);
                if(code != null){
                    Intent result = new Intent();
                    result.putExtra("code", code);
                    setResult(RESULT_OK, result);
                    finish(); 
					
                }
            }
        });

        // Load the page
        Intent intent = getIntent();
        if (intent.getData() != null) {
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl(intent.getDataString());
        }
    }

    private String getUrlParameter(String stringUrl, String key){
        Uri url = Uri.parse(stringUrl);
        return url.getQueryParameter(key);
    }
}
