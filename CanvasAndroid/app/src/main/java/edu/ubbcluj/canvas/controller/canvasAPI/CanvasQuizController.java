package edu.ubbcluj.canvas.controller.canvasAPI;

import android.content.Context;
import android.content.SharedPreferences;

import com.instructure.canvasapi.api.QuizAPI;
import com.instructure.canvasapi.model.Course;
import com.instructure.canvasapi.model.Quiz;
import com.instructure.canvasapi.utilities.APIStatusDelegate;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;
import com.instructure.canvasapi.utilities.LinkHeaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.ubbcluj.canvas.controller.ControllerFactory;
import edu.ubbcluj.canvas.controller.CoursesController;
import edu.ubbcluj.canvas.controller.QuizController;
import edu.ubbcluj.canvas.persistence.PersistentCookieStore;
import edu.ubbcluj.canvas.util.listener.InformationEvent;
import edu.ubbcluj.canvas.util.listener.InformationListener;
import edu.ubbcluj.canvas.view.activity.CourseActivity;
import retrofit.client.Response;

public class CanvasQuizController implements APIStatusDelegate, QuizController {
    private ArrayList<Quiz> data;
    private List<InformationListener> actionList;
    private SharedPreferences sp;
    private String nextURL;
    private CanvasCallback<Quiz[]> quizCanvasCallback;
    private Context context;
    private Course c;

    public CanvasQuizController() {
        actionList = new LinkedList<>();
        nextURL = "";
        quizCanvasCallback = new CanvasCallback<Quiz[]>(this) {
            @Override
            public void firstPage(Quiz[] courses, LinkHeaders linkHeaders, Response response) {
                data = new ArrayList<>();
                Collections.addAll(data, courses);
                notifyListeners();
            }
        };
    }

    @Override
    public void setSharedPreferences(SharedPreferences sp) {
        this.sp = sp;
        CanvasRestAdapter.setupInstance(context, sp.getString("token", null), sp.getString("domain", null));
    }

    @Override
    public void addInformationListener(InformationListener il) {
        actionList.add(il);
    }

    @Override
    public void removeInformationListener(InformationListener il) {
        actionList.remove(il);
    }

    /**
     * Notifies listeners that the data has been retrieved from the server, and it's conversion is finished.
     */
    public synchronized void notifyListeners() {
        for (InformationListener il : actionList) {
            il.onComplete(new InformationEvent(this));
        }
    }

    @Override
    public void clearData() {
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(sp);

        persistentCookieStore.clear();
    }

    public void makeAPICall(int courseID) {
        ControllerFactory cf = ControllerFactory.getInstance();
        CoursesController cc = cf.getCoursesController();
        ((CanvasCoursesController)cc).setContext(context);
        cc.setSharedPreferences(sp);
        ((CanvasCoursesController)cc).makeAPICall();
        c = ((CanvasCoursesController)cc).getCourseByID((long) courseID);
        getQuizzes();
    }

    public void getQuizzes() {
        //Check if the first api call has come back.
        if ("".equals(nextURL)) {
            QuizAPI.getFirstPageQuizzes(c, quizCanvasCallback);
        }
        //Check if we're at the end of the paginated list.
        else {
            if (nextURL != null) {
                QuizAPI.getNextPageQuizzes(nextURL, quizCanvasCallback);
            }
        }
    }

    @Override
    public List<Quiz> getData() {
        return data;
    }

    public void setContext(Context context) {
        this.context = context;
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
        return context;
    }

}
