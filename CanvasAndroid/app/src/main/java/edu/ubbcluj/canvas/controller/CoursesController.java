package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;

import com.instructure.canvasapi.model.Course;

import edu.ubbcluj.canvas.model.ActiveCourse;
import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface CoursesController {
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il); 

	List<ActiveCourse> getData();
	
	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
