package edu.ubbcluj.canvas.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.instructure.canvasapi.api.OAuthAPI;
import com.instructure.canvasapi.api.UserAPI;
import com.instructure.canvasapi.model.OAuthToken;
import com.instructure.canvasapi.model.User;
import com.instructure.canvasapi.utilities.APIHelpers;
import com.instructure.canvasapi.utilities.APIStatusDelegate;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;
import com.instructure.canvasapi.utilities.LinkHeaders;
import com.instructure.canvasapi.utilities.UserCallback;

import edu.ubbcluj.canvas.controller.ControllerFactory;
import edu.ubbcluj.canvas.controller.CoursesController;
import edu.ubbcluj.canvas.controller.canvasAPI.CanvasCoursesController;
import edu.ubbcluj.canvas.persistence.CourseProvider;
import edu.ubbcluj.canvas.persistence.ServiceProvider;
import edu.ubbcluj.canvas.persistence.model.SingletonSharedPreferences;
import edu.ubbcluj.canvas.util.listener.InformationEvent;
import edu.ubbcluj.canvas.util.listener.InformationListener;
import edu.ubbcluj.canvas.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.R;
import retrofit.client.Response;

public class LoginActivity extends Activity implements APIStatusDelegate {
	private ControllerFactory cf;
	private String username = "gcim1327@scs.ubbcluj.ro";
	private static String token = "";
	private UserCallback userCallback;
	public final static String DOMAIN = "canvas.cs.ubbcluj.ro";
	private final static String ID = "10000000000002";
	private final static String SECRET = "ru1UrVTFK3mHtiiVHiZghZqWQXNg12oOjjNjAyTcFBmEjlxGXm6yes7Ho4iPxGfH";
	private final static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Set the singleton context to make a global context
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		sPreferences.init(LoginActivity.this.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE));
		APIHelpers.setDefaultErrorDelegateClass(this, "edu.ubbcluj.canvas.controller.canvasAPI.CanvasErrorDelegate");
		APIHelpers.setDomain(this, DOMAIN);

		cf = ControllerFactory.getInstance();

		userCallback = new UserCallback(this) {
			@Override
			public void cachedUser(User user) {

			}

			@Override
			public void user(User user, Response response) {
				username = user.getLoginId();
				loginCompleted();
			}
		};
		login();
	}

	private void login() {
		//TO DO : handle login using  com.instructure.canvasapi.*;
		if (!CheckNetwork.isNetworkOnline(this)) {
			Toast.makeText(this, "No network connection!", Toast.LENGTH_LONG).show();
		} else {
			SharedPreferences sp = getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE);
			if (sp.getString("token", null) == null) {
				Intent intent = new Intent(LoginActivity.this, OAuthActivity.class);
				intent.setData(Uri.parse("https://" + DOMAIN + "/login/oauth2/auth?client_id=" + ID + "&response_type=code&redirect_uri=" + REDIRECT_URI));
				startActivityForResult(intent, 0);
			} else {
				UserAPI.getSelf(userCallback);
				CanvasRestAdapter.setupInstance(this, token, DOMAIN);
				//loginCompleted();
			}
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String authCode = (String) data.getExtras().get("code");
		try {
			setOAuthToken(ID, SECRET, authCode);
			//UserAPI.getSelf(userCallback);
			loginCompleted();
		}catch(Exception e){
			Log.e(APIHelpers.LOG_TAG,"ERROR: "+e.getMessage());
		}
	}

	private void setOAuthToken(String id, String devKey, String authResponseCode){
		OAuthAPI.getToken(id, devKey, authResponseCode, new CanvasCallback<OAuthToken>(LoginActivity.this) {

			@Override
			public void firstPage(OAuthToken oAuthToken, LinkHeaders linkHeaders, Response response) {
				token = oAuthToken.getAccess_token();
				SharedPreferences sp = getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE);
				sp.edit().putString("token", token).apply();
				sp.edit().putString("domain", DOMAIN).apply();
			}
		});
	}

	public void loginCompleted() {
		final CoursesController coursesController = cf.getCoursesController2();
		((CanvasCoursesController)coursesController).setContext(this);
		coursesController.setSharedPreferences(getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE));

		Log.d("logolunk", "ez itt mindig 2x hivodik meg es nem tudom hogy miert");
		coursesController.addInformationListener(new InformationListener() {

			@Override
			public void onComplete(InformationEvent e) {
				ServiceProvider sp = ServiceProvider.getInstance();
				CourseProvider cp = CourseProvider.getInstance();
				String username = LoginActivity.this.username.replace('@', '.');
				cp.initalize(LoginActivity.this, username);
				sp.initialize(getApplicationContext());
				cp.updateWith(coursesController.getData());
				redirect();
			}
		});
		((CanvasCoursesController)coursesController).makeAPICall();
	}

	// Redirect to dashboard
	public void redirect() {
		Intent dashBoardIntent = new Intent(this, DashBoardActivity.class);
		startActivity(dashBoardIntent);
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
}