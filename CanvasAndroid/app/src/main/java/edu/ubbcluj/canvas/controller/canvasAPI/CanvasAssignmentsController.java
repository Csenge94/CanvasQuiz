package edu.ubbcluj.canvas.controller.canvasAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.instructure.canvasapi.api.AssignmentAPI;
import com.instructure.canvasapi.model.Assignment;
//import edu.ubbcluj.canvas.model.Assignment;
import com.instructure.canvasapi.model.CanvasError;
import com.instructure.canvasapi.model.Course;
import com.instructure.canvasapi.utilities.APIStatusDelegate;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;
import com.instructure.canvasapi.utilities.ErrorDelegate;
import com.instructure.canvasapi.utilities.LinkHeaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.ubbcluj.canvas.controller.AssignmentsController;
import edu.ubbcluj.canvas.controller.ControllerFactory;
import edu.ubbcluj.canvas.controller.CoursesController;

import edu.ubbcluj.canvas.persistence.PersistentCookieStore;
import edu.ubbcluj.canvas.util.listener.InformationEvent;
import edu.ubbcluj.canvas.util.listener.InformationListener;
import edu.ubbcluj.canvas.view.activity.CourseActivity;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by David on 1/13/2016.
 */
public class CanvasAssignmentsController implements AssignmentsController, ErrorDelegate,APIStatusDelegate {

    private List<InformationListener> actionList;
    private List<edu.ubbcluj.canvas.model.Assignment> data;
    private List<Assignment> canvasAPIAssignments;
    private SharedPreferences sp;
    private Context context;
    private String nextURL;
    private CanvasCallback<Assignment[]> assignmentCanvasCallback;
    Course c;

    public CanvasAssignmentsController() {
        actionList = new LinkedList<>();
        nextURL = "";
        assignmentCanvasCallback = new CanvasCallback<Assignment[]>(this) {
            @Override
            public void firstPage(Assignment[] assignments, LinkHeaders linkHeaders, Response response) {
                data = new ArrayList<>();
                canvasAPIAssignments = new ArrayList<>();
                edu.ubbcluj.canvas.model.Assignment localAssignment;
                for (Assignment a : assignments) {
                    canvasAPIAssignments.add(a);
                    localAssignment = new edu.ubbcluj.canvas.model.Assignment();
                    localAssignment.setCourseId((int) c.getId());
                    localAssignment.setId((int) a.getId());
                    localAssignment.setName(a.getName());
                    localAssignment.setCourseName(c.getName());
                    localAssignment.setDescription(a.getDescription());
                    localAssignment.setDueAt(a.getDueDate().toString());
                    localAssignment.setHtmlUrl(a.getHtmlUrl());
                    localAssignment.setPosition(a.getPosition());
                    localAssignment.setPointsPossible(a.getPointsPossible());
                    data.add(localAssignment);
                }
                notifyListeners();
            }
        };
    }

    @Override
    public void addInformationListener(InformationListener il) {
        actionList.add(il);
    }

    @Override
    public void removeInformationListener(InformationListener il) {
        actionList.remove(il);
    }

    @Override
    public List<edu.ubbcluj.canvas.model.Assignment> getData() {
        return data;
    }

    @Override
    public void setSharedPreferences(SharedPreferences sp) {
        this.sp=sp;
       // CanvasRestAdapter.setupInstance(context, sp.getString("token", null), sp.getString("domain", null));
    }

    @Override
    public void clearData() {
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(sp);

        persistentCookieStore.clear();
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context=context;
    }

    public synchronized void notifyListeners() {
        for (InformationListener il : actionList) {
            il.onComplete(new InformationEvent(this));
        }
    }

    @Override
    public void noNetworkError(RetrofitError error, Context context) {

    }

    public void makeAPICall(int courseID) {
        CanvasCoursesController cc;
        ControllerFactory cf = ControllerFactory.getInstance();
        cc = cf.getCoursesController2();
        (cc).setContext(context);
        cc.setSharedPreferences(sp);
        Log.d("logolunk", "hivas elott");

        while (c == null) {
            Log.d("logolunk", "belement");
            cc.makeAPICall();
            c = cc.getCourseByID(courseID);

		}
        Log.d("logolunk", "kijott");
        AssignmentAPI.getAllAssignmentsExhaustive(c.getId(), assignmentCanvasCallback);

    }

//    public void getAllAssigments() {
//        //Check if the first api call has come back.
//        if ("".equals(nextURL)) {
//            AssignmentAPI.getAllAssignmentsExhaustive(c.getId(),assignmentCanvasCallback);
//        }
//        //Check if we're at the end of the paginated list.
//        else {
//            if (nextURL != null) {
//                QuizAPI.getNextPageQuizzes(nextURL, quizCanvasCallback);
//            }
//        }
//    }
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
