package edu.ubbcluj.canvas.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TtsSpan;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.instructure.canvasapi.api.QuizAPI;
import com.instructure.canvasapi.model.CanvasError;
import com.instructure.canvasapi.model.Course;
import com.instructure.canvasapi.model.Quiz;
import com.instructure.canvasapi.model.QuizAnswer;
import com.instructure.canvasapi.model.QuizQuestion;
import com.instructure.canvasapi.utilities.APIStatusDelegate;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;
import com.instructure.canvasapi.utilities.ErrorDelegate;
import com.instructure.canvasapi.utilities.LinkHeaders;

import java.util.ArrayList;
import retrofit.RetrofitError;

public class QuizActivity extends AppCompatActivity implements APIStatusDelegate, ErrorDelegate{

    private static Quiz quiz;
    private static Course course;
    private String nextURL;
    private ArrayList<QuizQuestion> quizQuestions;
    private CanvasCallback<QuizQuestion[]> quizQuestionsCanvasCallback;
    private int questionCount;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        course = ExampleActivity.getCourse();
        quiz = ExampleActivity.getQuiz();
        questionCount = quiz.getQuestionCount();
        CanvasRestAdapter.setupInstance(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token", null),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("domain", null));

        quizQuestions = new ArrayList<>();
        quizQuestionsCanvasCallback = new CanvasCallback<QuizQuestion[]>(QuizActivity.this) {
            @Override
            public void firstPage(QuizQuestion[] questions, LinkHeaders linkHeaders, retrofit.client.Response response) {
                //Save the next url for pagination.
                nextURL = linkHeaders.nextURL;
                for (QuizQuestion quizQuestion : questions) {
                    quizQuestions.add(quizQuestion);
                    questionCount--;
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
            QuizAPI.getFirstPageQuizQuestions(course, quiz.getId(), quizQuestionsCanvasCallback);     //we use the c course to get it's quizzes

        }
        //Check if we're at the end of the paginated list.
        else if (nextURL != null) {
            QuizAPI.getNextPageQuizQuestions(nextURL, quizQuestionsCanvasCallback);
        }
        //We are at the end of the list.
        else {
            Toast.makeText(getContext(), "There are no more items", Toast.LENGTH_LONG).show();
        }

    }

    private void showQuestions() {
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setScrollbarFadingEnabled(false);
        scrollView.addView(linearLayout);

        for (QuizQuestion quizQuestion : quizQuestions) {


            switch (quizQuestion.getQuestionType()) {
                case CALCULATED:
                    LinearLayout l7 = new LinearLayout(this);
                    l7.setOrientation(LinearLayout.VERTICAL);
                    TextView tw7 = new TextView(this);
                    tw7.setText(quizQuestion.getQuestionText());
                    l7.addView(tw7);
                    EditText edtext = new EditText(this);
                    l7.addView(edtext);
                    linearLayout.addView(l7);
                    break;
                case ESSAY:
                    //Mukodik :)
                    LinearLayout l6 = new LinearLayout(this);
                    l6.setOrientation(LinearLayout.VERTICAL);
                    TextView tw6 = new TextView(this);
                    tw6.setText(quizQuestion.getQuestionText());
                    l6.addView(tw6);
                    EditText edittext = new EditText(this);
                    l6.addView(edittext);
                    linearLayout.addView(l6);
                    break;
                case FILE_UPLOAD:
                    break;
                case FILL_IN_MULTIPLE_BLANKS:
                    //Mukodik :)
                    LinearLayout l5 = new LinearLayout(this);
                    l5.setOrientation(LinearLayout.VERTICAL);
                    TextView tw5 = new TextView(this);
                    tw5.setText(quizQuestion.getQuestionText());
                    l5.addView(tw5);

                    QuizAnswer[] answers_blank = quizQuestion.getAnswers();
                    long l = answers_blank[answers_blank.length-1].getBlankId();
                    for(int i = 0; i < l; i++) {
                        EditText text = new EditText(this);
                        l5.addView(text);
                    }
                    linearLayout.addView(l5);
                    break;
                case MATCHING:
                    //Nem mukodik :(
                  /*  LinearLayout l7 = new LinearLayout(this);
                    l7.setOrientation(LinearLayout.VERTICAL);
                    TextView tw7 = new TextView(this);
                    tw7.setText(quizQuestion.getQuestionText());
                    l7.addView(tw7);
                    QuizAnswer[] answers3 = quizQuestion.getAnswers();

                    for(int i = 0; i < answers3.length; i++){
                        TextView tw8 = new TextView(this);
                        tw8.setText(answers3[i].getAnswerText());
                        l7.addView(tw8);
                        Log.e("BlankId " + i + ": ", answers3[i].getBlankId() + "");
                        Log.e("Left " + i + ": ", answers3[i].getAnswerText() + "");
                    }*/

                    /*int i = 0;
                    int j = 0;
                    while (i <= answers2.length) {
                        Spinner spinner = new Spinner(this);
                        ArrayList<String> list = new ArrayList<String>();
                        long blankID = answers2[i].getBlankId();
                        j = i;
                        int k = 0;
                        while(j < answers2.length && answers2[j].getBlankId() == blankID) {
                            list.add(k, answers2[j].getAnswerText().toString());
                            k++;
                            j++;
                        }
                        i += j;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        l7.addView(spinner);
                    }
                    linearLayout.addView(l7);
                   */ break;
                case MULTIPLE_ANSWERS:
                    //MUKODIK :)
                    LinearLayout l0 = new LinearLayout(this);
                    l0.setOrientation(LinearLayout.VERTICAL);
                    TextView tw0 = new TextView(this);
                    tw0.setText(quizQuestion.getQuestionText());
                    l0.addView(tw0);
                    QuizAnswer[] answers1 = quizQuestion.getAnswers();
                    for (int i = 0; i < answers1.length; i++) {
                        CheckBox checkbox = new CheckBox(this);
                        checkbox.setText(answers1[i].getAnswerText());
                        checkbox.getAnimation();
                        l0.addView(checkbox);
                    }
                    linearLayout.addView(l0);
                    break;
                case MUTIPLE_CHOICE:
                    //MUKODIK :)
                    LinearLayout l1 = new LinearLayout(this);
                    l1.setOrientation(LinearLayout.VERTICAL);
                    TextView tw = new TextView(this);
                    tw.setText(quizQuestion.getQuestionText());
                    l1.addView(tw);
                    QuizAnswer[] answers = quizQuestion.getAnswers();
                    RadioGroup radioGroup = new RadioGroup(this);
                    for (int i = 0; i < answers.length; i++) {
                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setText(answers[i].getAnswerText());
                        radioGroup.addView(radioButton);
                    }
                    l1.addView(radioGroup);
                    linearLayout.addView(l1);
                    break;
                case MULTIPLE_DROPDOWNS:
                    //MUKODIK :)
                    LinearLayout l2 = new LinearLayout(this);
                    l2.setOrientation(LinearLayout.VERTICAL);
                    TextView tw2 = new TextView(this);
                    tw2.setText(quizQuestion.getQuestionText());
                    l2.addView(tw2);
                    QuizAnswer[] answers2 = quizQuestion.getAnswers();
                    int i = 0;
                    int j = 0;
                    while (i <= answers2.length) {
                        Spinner spinner = new Spinner(this);
                        ArrayList<String> list = new ArrayList<String>();
                        long blankID = answers2[i].getBlankId();
                        j = i;
                        int k = 0;
                        while(j < answers2.length && answers2[j].getBlankId() == blankID) {
                            list.add(k, answers2[j].getAnswerText().toString());
                            k++;
                            j++;
                        }
                        i += j;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        l2.addView(spinner);
                    }
                    linearLayout.addView(l2);
                    break;
                case NUMERICAL:
                    break;
                case SHORT_ANSWER:
                    break;
                case TEXT_ONLY:
                    //Nem mukodik meg, nem biztos, hogy van olyan kerdes a quiz.ben
               /*     LinearLayout l3 = new LinearLayout(this);
                    l3.setOrientation(LinearLayout.VERTICAL);
                    TextView tw3 = new TextView(this);
                    tw3.setText(quizQuestion.getQuestionText());
                    l3.addView(tw3);
                    EditText text = new EditText(this);
                    l3.addView(text);
                    linearLayout.addView(l3);
                 */   break;
                case TRUE_FALSE:
                    //MUKODIK :)
                    LinearLayout l4 = new LinearLayout(this);
                    l4.setOrientation(LinearLayout.VERTICAL);
                    TextView tw4 = new TextView(this);
                    tw4.setText(quizQuestion.getQuestionText());
                    l4.addView(tw4);
                    RadioGroup radioGroup1 = new RadioGroup(this);
                    RadioButton radioButton1 = new RadioButton(this);
                    RadioButton radioButton2 = new RadioButton(this);
                    radioButton1.setText("True");
                    radioButton2.setText("False");
                    radioGroup1.addView(radioButton1);
                    radioGroup1.addView(radioButton2);
                    l4.addView(radioGroup1);
                    linearLayout.addView(l4);
                    break;
                case UNKNOWN:
                    break;
            }
        }
        setContentView(scrollView);
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
