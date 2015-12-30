package edu.ubbcluj.canvas.controller.canvasAPI;

import android.content.Context;
import android.content.SharedPreferences;

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
    private List<InformationListener> actionList;
    private SharedPreferences sp;
    private String nextURL = "";
    private CanvasCallback<Course[]> courseCanvasCallback;
    private Context context;

    public CanvasCoursesController() {
        actionList = new LinkedList<>();
        courseCanvasCallback = new CanvasCallback<Course[]>(this) {
            @Override
            public void firstPage(Course[] courses, LinkHeaders linkHeaders, Response response) {
                data = new ArrayList<>();
                for (Course c : courses) {
                    data.add(new ActiveCourse((int)c.getId(), c.getName(), c.isFavorite()));
                }
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

    public void makeAPICall() {
        //Check if the first api call has come back.
        if ("".equals(nextURL)) {
            CourseAPI.getFirstPageCourses(courseCanvasCallback);
        }
        //Check if we're at the end of the paginated list.
        else {
            if (nextURL != null) {
                CourseAPI.getNextPageCourses(courseCanvasCallback, nextURL);
            }
        }
    }

    @Override
    public List<ActiveCourse> getData() {
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
