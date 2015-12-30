package edu.ubbcluj.canvas.controller.canvasAPI;

import android.content.Context;
import android.content.SharedPreferences;

import com.instructure.canvasapi.api.StreamAPI;
import com.instructure.canvasapi.model.StreamItem;
import com.instructure.canvasapi.utilities.APIStatusDelegate;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.LinkHeaders;

import java.util.ArrayList;
import java.util.List;

import edu.ubbcluj.canvas.controller.ActivityStreamController;
import edu.ubbcluj.canvas.model.ActivityStream;
import edu.ubbcluj.canvas.persistence.PersistentCookieStore;
import edu.ubbcluj.canvas.util.listener.InformationEvent;
import edu.ubbcluj.canvas.util.listener.InformationListener;
import retrofit.client.Response;

public class CanvasDashboardController implements ActivityStreamController, APIStatusDelegate {
    private List<ActivityStream> data;
    private List<InformationListener> actionList;
    private SharedPreferences sp;
    private String nextURL;
    private CanvasCallback<StreamItem[]> streamCanvasCallback;
    private Context context;

    public CanvasDashboardController() {
        super();
        actionList = new ArrayList<>();
        nextURL = "";
        streamCanvasCallback = new CanvasCallback<StreamItem[]>(this) {
            @Override
            public void firstPage(StreamItem[] streamItems, LinkHeaders linkHeaders, Response response) {
                for (StreamItem c : streamItems) {
                    String type = "";
                    switch (c.getType()) {
                        case DISCUSSION_TOPIC:
                            type = "DiscussionTopic";
                            break;
                        case SUBMISSION:
                            break;
                        case ANNOUNCEMENT:
                            type = "Announcement";
                            break;
                        case CONVERSATION:
                            type = "Conversation";
                            break;
                        case MESSAGE:
                            type = "Message";
                            break;
                        case UNKNOWN:
                            type = "Unknown";
                            break;
                        case NOT_SET:
                            type = "NotSet";
                            break;
                    }
                    ActivityStream as = new ActivityStream((int) c.getId(), c.getTitle(context), c.getMessage(context), type);
                    as.setCourseId((int) c.getCourseId());
                    as.setSecondaryId((int) c.getAssignmentId());
                    as.setRead_state(c.isReadState());
                    data.add(as);
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

    /**
     * Notifies listeners that the data has been retrieved from the server, and it's conversion is finished.
     */
    public synchronized void notifyListeners() {
        for (InformationListener il : actionList) {
            il.onComplete(new InformationEvent(this));
        }
    }

    @Override
    public List<ActivityStream> getData() {
        return data;
    }

    @Override
    public void setSharedPreferences(SharedPreferences sp) {
        this.sp = sp;
    }

    @Override
    public void clearData() {
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(sp);
        persistentCookieStore.clear();
    }

    public void makeApiCall() {
        data = new ArrayList<>();

        //Check if the first api call has come back.
        if ("".equals(nextURL)) {
            StreamAPI.getFirstPageUserStream(streamCanvasCallback);
        }
        //Check if we're at the end of the paginated list.
        else {
            if (nextURL != null) {
                StreamAPI.getNextPageStream(nextURL, streamCanvasCallback);
            }
        }
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