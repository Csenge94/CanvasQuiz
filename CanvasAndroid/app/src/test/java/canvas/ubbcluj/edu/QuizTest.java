package canvas.ubbcluj.edu;

import android.test.ActivityInstrumentationTestCase2;
import com.instructure.canvasapi.model.QuizQuestion;

import java.util.ArrayList;
import edu.ubbcluj.canvas.view.activity.QuizActivity;

public class QuizTest extends ActivityInstrumentationTestCase2<QuizActivity> {
    QuizActivity quizActivity;

    public QuizTest() {
        super(QuizActivity.class);
        quizActivity = getActivity();
    }

    public void QuizQuestionTest() {
        quizActivity.getQuizQuestions();
        assertNotNull(quizActivity.getQuestionsArray());
        for (QuizQuestion quizQuestion: (ArrayList<QuizQuestion>)quizActivity.getQuestionsArray()) {
            assertNotNull(quizQuestion);
        }
    }
}
