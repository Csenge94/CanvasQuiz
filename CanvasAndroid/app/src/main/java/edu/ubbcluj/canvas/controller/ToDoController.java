package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvas.model.Assignment;
import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface ToDoController {
	void addInformationListener(InformationListener il);
	
	void removeInformationListener(InformationListener il);

	List<Assignment> getData();	

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
