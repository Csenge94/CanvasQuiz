package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvas.model.MessageSequence;
import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface MessageSequenceController {
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<MessageSequence> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
