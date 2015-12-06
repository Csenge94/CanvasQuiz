package edu.ubbcluj.canvas.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.instructure.canvasapi.api.QuizAPI;
import com.instructure.canvasapi.model.CanvasError;
import com.instructure.canvasapi.model.Course;
import com.instructure.canvasapi.model.Quiz;
import com.instructure.canvasapi.model.QuizQuestion;
import com.instructure.canvasapi.utilities.APIStatusDelegate;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;
import com.instructure.canvasapi.utilities.ErrorDelegate;
import com.instructure.canvasapi.utilities.LinkHeaders;

import java.util.ArrayList;

import edu.ubbcluj.canvasAndroid.R;
import retrofit.RetrofitError;

public class QuizActivity extends AppCompatActivity implements APIStatusDelegate, ErrorDelegate{

    private static Quiz quiz;
    private static Course course;
    private String nextURL;
    private ArrayList<QuizQuestion> quizQuestions;
    private CanvasCallback<QuizQuestion[]> quizQuestionsCanvasCallback;
    private int questionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        course = ExampleActivity.getCourse();
        quiz = ExampleActivity.getQuiz();
        questionCount = quiz.getQuestionCount();
        CanvasRestAdapter.setupInstance(this, ExampleActivity.token, ExampleActivity.DOMAIN);
        Log.d("logolunk", "oncreate");

        quizQuestions = new ArrayList<>();
        quizQuestionsCanvasCallback = new CanvasCallback<QuizQuestion[]>(QuizActivity.this) {
            @Override
            public void firstPage(QuizQuestion[] questions, LinkHeaders linkHeaders, retrofit.client.Response response) {
                //Save the next url for pagination.
                nextURL = linkHeaders.nextURL;
                Log.d("Logolunk", "getQuizQuestions:" + nextURL);
                for (QuizQuestion quizQuestion : questions) {
                    quizQuestions.add(quizQuestion);
                    questionCount--;
                    Log.d("logolunk", quizQuestion.getQuestionType() + "");
                    if (0 == questionCount) {
                        showQuestions();
                    }
                }
            }

            @Override
            public void nextPage(QuizQuestion[] questions, LinkHeaders linkHeaders, retrofit.client.Response response) {
                //nextPage is an optional override. The default behavior is to simply call firstPage() as we've done here;
                firstPage(questions, linkHeaders, response);
            }
        };
        nextURL = "";
        getQuizQuestions();
    }

    public void getQuizQuestions() {    //get the quizzes (it will have another name)
        //Check if the first api call has come back.
       if ("".equals(nextURL)) {
            Log.d("logolunk", course.getName() + " " + quiz.getTitle());
            QuizAPI.getFirstPageQuizQuestions(course, quiz.getId(), quizQuestionsCanvasCallback);     //we use the c course to get it's quizzes

        }
        //Check if we're at the end of the paginated list.
        else if (nextURL != null) {
            Log.d("logolunk", "4");

            QuizAPI.getNextPageQuizQuestions(nextURL, quizQuestionsCanvasCallback);
            Log.d("logolunk", "5");

        }
        //We are at the end of the list.
        else {
            Log.d("logolunk", "6");

            Toast.makeText(getContext(), "There are no more items", Toast.LENGTH_LONG).show();
            Log.d("logolunk", "7");

        }

    }

    private void showQuestions() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (QuizQuestion quizQuestion : quizQuestions) {
            TextView tw = new TextView(this);
            tw.setText(quizQuestion.getQuestionText());
            linearLayout.addView(tw);
            switch (quizQuestion.getQuestionType()) {
                case CALCULATED:

                    break;
                case ESSAY:
                    break;
                case FILE_UPLOAD:
                    break;
                case FILL_IN_MULTIPLE_BLANKS:
                    break;
                case MATCHING:
                    break;
                case MULTIPLE_ANSWERS:
                    break;
                case MUTIPLE_CHOICE:
                   /* for (int i = 0; i < quizQuestion.getAnswers().length; i++) {

                    }*/
                    break;
                case MULTIPLE_DROPDOWNS:
                    break;
                case NUMERICAL:
                    break;
                case SHORT_ANSWER:
                    break;
                case TEXT_ONLY:
                    break;
                case TRUE_FALSE:
                    break;
                case UNKNOWN:
                    break;
            }
        }
        setContentView(linearLayout);
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
        return this;
    }

    @Override
    public void noNetworkError(RetrofitError error, Context context) {

    }

    @Override
    public void notAuthorizedError(RetrofitError error, CanvasError canvasError, Context context) {

    }

    @Override
    public void invalidUrlError(RetrofitError error, Context context) {

    }

    @Override
    public void serverError(RetrofitError error, Context context) {

    }

    @Override
    public void generalError(RetrofitError error, CanvasError canvasError, Context context) {

    }
}
