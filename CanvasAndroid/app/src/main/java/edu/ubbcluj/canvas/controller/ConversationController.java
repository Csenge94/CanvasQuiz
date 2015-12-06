package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvas.model.Conversation;
import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface ConversationController {

	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<Conversation> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
