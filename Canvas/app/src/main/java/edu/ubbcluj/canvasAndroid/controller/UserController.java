package edu.ubbcluj.canvasAndroid.controller;

import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import edu.ubbcluj.canvasAndroid.view.activity.LoginActivity;

public interface UserController {
	String loginUser(String host);

	void setUsername(String username);

	String getUsername();
	
	String getLastUsername();

	void setPassword(String password);

	String getPassword();

	void setLoginActivity(LoginActivity loginActivity);

	LoginActivity getLoginActivity();

	void setSharedPreferences(SharedPreferences sp);

	ArrayAdapter<String> getSavedUsersAdapter();
}
