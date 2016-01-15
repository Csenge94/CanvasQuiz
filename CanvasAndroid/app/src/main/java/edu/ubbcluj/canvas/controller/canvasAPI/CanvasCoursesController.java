package edu.ubbcluj.canvas.controller.canvasAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.instructure.canvasapi.api.CourseAPI;
import com.instructure.canvasapi.model.Course;
import com.instructure.canvasapi.utilities.APIStatusDelegate;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;
import com.instructure.canvasapi.utilities.LinkHeaders;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.ubbcluj.canvas.controller.CoursesController;
import edu.ubbcluj.canvas.model.ActiveCourse;
import edu.ubbcluj.canvas.persistence.PersistentCookieStore;
import edu.ubbcluj.canvas.util.listener.InformationEvent;
import edu.ubbcluj.canvas.util.listener.InformationListener;
import retrofit.client.Response;

public class CanvasCoursesController implements APIStatusDelegate, CoursesController {
    private ArrayList<ActiveCourse> data;
    private ArrayList<Course> canvasApiCourses;
    private List<InformationListener> actionList;
    private SharedPreferences sp;
    private CanvasCallback<Course[]> courseCanvasCallback;
    private Context context;

    public CanvasCoursesController() {
        actionList = new LinkedList<>();
        courseCanvasCallback = new CanvasCallback<Course[]>(this) {
            @Override
            public void firstPage(Course[] courses, LinkHeaders linkHeaders, Response response) {
               for (Course c : courses) {
                    canvasApiCourses.add(c);
                    data.add(new ActiveCourse((int)c.getId(), c.getName(), c.isFavorite()));
                }
                notifyListeners();
            }
        };
        data = new ArrayList<>();
        canvasApiCourses = new ArrayList<>();
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

    public void makeAPICall() {
            CourseAPI.getAllFavoriteCourses(courseCanvasCallback);
    }

    @Override
    public List<ActiveCourse> getData() {
        return data;
    }

    public List<Course> getCourses() {
        return canvasApiCourses;
    }


    public void setContext(Context context) {

        Log.d("logolunk", "canvas courses controller set context");

        this.context = context;
        Log.d("logolunk", "canvas courses controller set context end");

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

    public Course getCourseByID(long id) {
        for (Course c: canvasApiCourses) {
            Log.d("logolunk", c.getId() + " itt vagyok");
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

}
