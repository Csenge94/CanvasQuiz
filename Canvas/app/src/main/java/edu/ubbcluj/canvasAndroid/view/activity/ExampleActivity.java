package edu.ubbcluj.canvasAndroid.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.instructure.canvasapi.api.CourseAPI;
import com.instructure.canvasapi.model.CanvasError;
import com.instructure.canvasapi.model.Course;
import com.instructure.canvasapi.utilities.*;

import edu.ubbcluj.canvasAndroid.R;
import retrofit.RetrofitError;

public class ExampleActivity extends Activity implements APIStatusDelegate, ErrorDelegate {
    /**
     * CanvasAPI member variables.
     */

    //TODO: Change these to be valid for you.
    //http://guides.instructure.com/s/2204/m/4214/l/40399-how-do-i-obtain-an-api-access-token

    public final static String DOMAIN = "canvas.cs.ubbcluj.ro";
    public final static String TOKEN = "ZzJUOBf7CxiXYHjU5n18nJc03IwFsu2IoCsPHfGtwZxn4c85UmkqeFl4JS1PAwnM";

    public final static String SECTION_DIVIDER = " \n \n ------------------- \n \n";

    /**
     * Activity member variables.
     */
    CanvasCallback<Course[]> courseCanvasCallback;
    String nextURL = "";
    ScrollView scrollView;
    Button loadNextURLButton;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        output = (TextView)findViewById(R.id.output);
        loadNextURLButton = (Button) findViewById(R.id.loadNextPage);
        scrollView = (ScrollView) findViewById(R.id.scrollview);

        //Set up CanvasAPI
        setUpCanvasAPI();

        //Set up the callback
        courseCanvasCallback = new CanvasCallback<Course[]>(this) {
            @Override
            public void cache(Course[] courses) {
                //Cache will ALWAYS come before firstPage.
                //Only the firstPage of any API is ever cached.
                for (Course course : courses) {
                    appendToTextView("[CACHED] " + course.getId() + ": " + course.getName());
                }
                appendToTextView(SECTION_DIVIDER);
            }

            @Override
            public void firstPage(Course[] courses, LinkHeaders linkHeaders, retrofit.client.Response response) {
                //Save the next url for pagination.
                nextURL = linkHeaders.nextURL;

                for (Course course : courses) {
                    appendToTextView(course.getId() + ": " + course.getName());
                }
            }

            @Override
            public void nextPage(Course[] courses, LinkHeaders linkHeaders, retrofit.client.Response response) {
                //nextPage is an optional override. The default behavior is to simply call firstPage() as we've done here;
                firstPage(courses, linkHeaders, response);
            }
        };


        //If they press the button, make an API call.
        loadNextURLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAPICall();
            }
        });

        //Make the actual API call.
        makeAPICall();
    }

    /**
     * Helper for making an API call.
     */

    public void makeAPICall() {
        //Don't let them spam the button.
        loadNextURLButton.setEnabled(false);

        //Check if the first api call has come back.
        if ("".equals(nextURL)) {
            CourseAPI.getFirstPageCourses(courseCanvasCallback);
        }
        //Check if we're at the end of the paginated list.
        else if (nextURL != null) {
            CourseAPI.getNextPageCourses(courseCanvasCallback, nextURL);
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
        CanvasRestAdapter.setupInstance(this, TOKEN, DOMAIN);

        //Set up a default error delegate. This will be the same one for all API calls
        //You can override the default ErrorDelegate in any CanvasCallBack constructor.
        //In a real application, this should probably be a standalone class.
        APIHelpers.setDefaultErrorDelegateClass(this, this.getClass().getName());
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