package edu.ubbcluj.canvas.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import edu.ubbcluj.canvas.controller.canvasAPI.CanvasDashboardController;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvas.controller.ActivityStreamController;
import edu.ubbcluj.canvas.controller.ControllerFactory;
import edu.ubbcluj.canvas.controller.rest.RestInformation;
import edu.ubbcluj.canvas.model.ActivityStream;
import edu.ubbcluj.canvas.util.listener.InformationEvent;
import edu.ubbcluj.canvas.util.listener.InformationListener;
import edu.ubbcluj.canvas.util.network.CheckNetwork;
import edu.ubbcluj.canvas.view.adapter.CustomArrayAdapterActivityStream;

public class DashBoardActivity extends BaseActivity {

	private ControllerFactory cf;
	private List<ActivityStream> activityStream;

	private CustomArrayAdapterActivityStream adapter;
	
	private ListView list;
	private View viewContainer;

	private SwipeRefreshLayout swipeView = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the progressbar visibility 
		list = (ListView) findViewById(R.id.list);
		viewContainer = findViewById(R.id.linProg);
		viewContainer.setVisibility(View.VISIBLE);

		// Get the activity stream
		cf = ControllerFactory.getInstance();
		
		ActivityStreamController dashboardController;
		dashboardController = cf.getDashboardController2();
		((CanvasDashboardController)dashboardController).setContext(this);
		dashboardController.setSharedPreferences(DashBoardActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));
		
		activityStream = new ArrayList<>();

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ActivityStream as = activityStream.get(position);

				if (swipeView != null)
					swipeView.setRefreshing(false);

				if (!CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
					Toast.makeText(DashBoardActivity.this, "No network connection!", Toast.LENGTH_LONG).show();
				} else {
					as.setRead_state(true);
					activityStream.set(position, as);
					if (as.getType().equals("Announcement")) {
						Intent informationIntent = new Intent(DashBoardActivity.this, AnnouncementActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("announcement_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}

					if (as.getType().equals("Submission")) {
						Intent informationIntent = new Intent(DashBoardActivity.this, AssignmentActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("assignment_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}

					if (as.getType().equals("Message")) {
						Intent informationIntent = new Intent(DashBoardActivity.this, AssignmentActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("assignment_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}

					if (as.getType().equals("Conversation")) {
						Intent messagesItemIntent = new Intent(DashBoardActivity.this, MessageItemActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("id", as.getId());
						messagesItemIntent.putExtras(bundle);
						startActivity(messagesItemIntent);
					}

					if (as.getType().equals("DiscussionTopic")) {
						Intent informationIntent = new Intent(DashBoardActivity.this, AnnouncementActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("announcement_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}
				}
			}
		});

		dashboardController.addInformationListener(new InformationListener() {
			@Override
			public void onComplete(InformationEvent e) {
				ActivityStreamController asd = (ActivityStreamController)e.getSource();
				setProgressGone();
				setActivityStream(asd.getData());
			}
		});

		((CanvasDashboardController)dashboardController).makeApiCall();

		// Initialize the dashboard list
		setList();
		
		swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        	
        	@Override
        	public void onRefresh() {
        		if(!CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
        			swipeView.setRefreshing(false);
					Toast.makeText(DashBoardActivity.this, "No network connection!", Toast.LENGTH_LONG).show();
        		} else {
	        		ActivityStreamController dashboardController;
	        		dashboardController = cf.getDashboardController2();
					((CanvasDashboardController)dashboardController).setContext(DashBoardActivity.this);
	        		dashboardController.setSharedPreferences(DashBoardActivity.this.getSharedPreferences(
							"CanvasAndroid", Context.MODE_PRIVATE));
	        		RestInformation.clearData();
	        		
	        		dashboardController.addInformationListener(new InformationListener() {

						@Override
						public void onComplete(InformationEvent e) {
							ActivityStreamController asd = (ActivityStreamController) e.getSource();
							setProgressGone();
							setActivityStream(asd.getData());
							swipeView.setRefreshing(false);
							setList();
						}

					});
					((CanvasDashboardController)dashboardController).makeApiCall();
				}
        	} 	
    	});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(adapter!=null)
			setList();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	public void setList() {
		if (adapter == null) {
			// Initialize the empty list
			adapter = new CustomArrayAdapterActivityStream(this, activityStream);
			list.setAdapter(adapter);
		} else {
			// Clear list and add new items
			adapter.clear();
			for (ActivityStream as : activityStream) {
				adapter.add(as);
			}
			adapter.notifyDataSetChanged();
		}
	}

/*	public List<ActivityStream> getActivityStream() {
		return activityStream;
	}
*/
	public void setActivityStream(List<ActivityStream> activityStream) {
		this.activityStream = activityStream;
		setList();
	}

	// Hide progressbar
	public void setProgressGone() {
		viewContainer.setVisibility(View.GONE);
	}
}
