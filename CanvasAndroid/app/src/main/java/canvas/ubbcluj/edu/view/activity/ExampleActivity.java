package edu.ubbcluj.canvasAndroid.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.instructure.canvasapi.api.ConversationAPI;
import com.instructure.canvasapi.api.OAuthAPI;
import com.instructure.canvasapi.model.CanvasError;
import com.instructure.canvasapi.model.Conversation;
import com.instructure.canvasapi.model.OAuthToken;
import com.instructure.canvasapi.utilities.*;

import edu.ubbcluj.canvasAndroid.R;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ExampleActivity extends Activity implements APIStatusDelegate, ErrorDelegate {
    private static String authCode;
    private static String token;

    private final static String DOMAIN = "canvas.cs.ubbcluj.ro";
    private final static String ID = "10000000000002";
    private final static String SECRET = "ru1UrVTFK3mHtiiVHiZghZqWQXNg12oOjjNjAyTcFBmEjlxGXm6yes7Ho4iPxGfH";
    private final static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    public final static String SECTION_DIVIDER = " \n \n ------------------- \n \n";

    /**
     * Activity member variables.
     */
    CanvasCallback<Conversation[]> conversationCanvasCallback;
    String nextURL = "";
    ScrollView scrollView;
    Button loadNextURLButton;
    TextView output;
    ExampleActivity exampleActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        output = (TextView)findViewById(R.id.output);
        loadNextURLButton = (Button) findViewById(R.id.loadNextPage);
        scrollView = (ScrollView) findViewById(R.id.scrollview);


        //Set up a default error delegate. This will be the same one for all API calls
        //You can override the default ErrorDelegate in any CanvasCallBack constructor.
        //In a real application, this should probably be a standalone class.
        APIHelpers.setDefaultErrorDelegateClass(this, this.getClass().getName());
        APIHelpers.setDomain(this,DOMAIN);
        //Set up the callback
        conversationCanvasCallback = new CanvasCallback<Conversation[]>(this) {
            @Override
            public void cache(Conversation[] conversations) {
                //Cache will ALWAYS come before firstPage.
                //Only the firstPage of any API is ever cached.
                for (Conversation conversation : conversations) {
                    appendToTextView("[CACHED] " + conversation.getId() + ": " + conversation.getLastMessagePreview());
                }
                appendToTextView(SECTION_DIVIDER);
            }

            @Override
            public void firstPage(Conversation[] conversations, LinkHeaders linkHeaders, retrofit.client.Response response) {
                //Save the next url for pagination.
                nextURL = linkHeaders.nextURL;

                for (Conversation conversation : conversations) {
                    appendToTextView(conversation.getId() + ": " + conversation.getLastMessagePreview());
                }
            }

            @Override
            public void nextPage(Conversation[] conversations, LinkHeaders linkHeaders, retrofit.client.Response response) {
                //nextPage is an optional override. The default behavior is to simply call firstPage() as we've done here;
                firstPage(conversations, linkHeaders, response);
            }
        };

        //If they press the button, make an API call.
        loadNextURLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAPICall();
            }
        });
        final ExampleActivity exampleAc = this;

        Button loginButton = (Button) findViewById(R.id.login2button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(exampleAc, OAuthActivity.class);
                intent.setData(Uri.parse("https://"+DOMAIN +"/login/oauth2/auth?client_id=" + ID + "&response_type=code&redirect_uri=" + REDIRECT_URI));
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        findViewById(R.id.login2button).setVisibility(View.INVISIBLE);
        findViewById(R.id.loadNextPage).setVisibility(View.VISIBLE);
        authCode = (String) data.getExtras().get("code");
        Log.d(APIHelpers.LOG_TAG, "code: " + authCode);
        try {
            setOAuthToken(ID, SECRET, authCode);
        }catch(Exception e){
            Log.e(APIHelpers.LOG_TAG,"ERROR: "+e.getMessage());
        }
    }

    private void setOAuthToken(String id, String devKey, String authResponseCode){
        OAuthAPI.getToken(id, devKey, authResponseCode, new CanvasCallback<OAuthToken>(this) {
            @Override
            public void cache(OAuthToken oAuthToken) {

            }

            @Override
            public void firstPage(OAuthToken oAuthToken, LinkHeaders linkHeaders, Response response) {
                Log.d(APIHelpers.LOG_TAG,"Token: "+ oAuthToken.getAccess_token());
                token = oAuthToken.getAccess_token();
                setUpCanvasAPI();
                makeAPICall();
            }
        });
    }


    /**
     * Helper for making an API call.
     */

    public void makeAPICall() {
        //Don't let them spam the button.
        loadNextURLButton.setEnabled(false);

        //Check if the first api call has come back.
        if ("".equals(nextURL)) {
            ConversationAPI.getFirstPageConversations(conversationCanvasCallback, ConversationAPI.ConversationScope.ALL);
        }
        //Check if we're at the end of the paginated list.
        else if (nextURL != null) {
            ConversationAPI.getNextPageConversations(conversationCanvasCallback, nextURL);
        }
        //We are at the end of the list.
        else {
            Toast.makeText(getContext(), "There are no more items", Toast.LENGTH_LONG).show();
            loadNextURLButton.setEnabled(true);
        }
    }

    /**
     * Helper for appending to a text view
     */

    public void appendToTextView(String text){
        if(output == null){
            output = (TextView)findViewById(R.id.output);
        }

        String current = output.getText().toString();
        output.setText(current + "\n" + text);

        //Scroll to the bottom of the scrollview.
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    /**
     * This is all stuff that should only need to be called once for the entire project.
     */
    public void setUpCanvasAPI() {
        //Set up the Canvas Rest Adapter.
        CanvasRestAdapter.setupInstance(this, token, DOMAIN);
    }



    /**
     * ApiStatusDelegate Overrides.
     */

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
        return this;
    }

    /**
     * Error Delegate Overrides.
     */


    @Override
    public void noNetworkError(RetrofitError retrofitError, Context context) {
        //Check the logcat for additional information
        Log.d(APIHelpers.LOG_TAG, "There was no network");
    }

    @Override
    public void notAuthorizedError(RetrofitError retrofitError, CanvasError canvasError, Context context) {
        //Check the logcat for additional information
        Log.d(APIHelpers.LOG_TAG, "HTTP 401");
    }

    @Override
    public void invalidUrlError(RetrofitError retrofitError, Context context) {
        //Check the logcat for additional information
        Log.d(APIHelpers.LOG_TAG, "HTTP 404");
    }

    @Override
    public void serverError(RetrofitError retrofitError, Context context) {
        //Check the logcat for additional information
        Log.d(APIHelpers.LOG_TAG, "HTTP 500");
    }

    public void generalError(RetrofitError retrofitError, CanvasError canvasError, Context context) {
        //Check the logcat for additional information
        Log.d(APIHelpers.LOG_TAG, "HTTP 200 but something went wrong. Probably a GSON parse error.");
    }
}