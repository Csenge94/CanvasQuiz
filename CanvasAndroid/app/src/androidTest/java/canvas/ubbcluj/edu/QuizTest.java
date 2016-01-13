package canvas.ubbcluj.edu;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import edu.ubbcluj.canvas.view.activity.CourseActivity;
import edu.ubbcluj.canvas.view.activity.QuizActivity;

//show the list of quzzes in course page
public class QuizTest extends ActivityInstrumentationTestCase2<CourseActivity> {

    private CourseActivity courseActivity;

    public QuizTest() {
        super(CourseActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        courseActivity = getActivity();
       /* mFirstTestText =
                (TextView) mFirstTestActivity
                        .findViewById(R.id.my_first_test_text_view);
   */ }
}
