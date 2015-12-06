package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvas.model.FileTreeElement;
import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface FolderController {

	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<FileTreeElement> getData();

	void setSharedPreferences(SharedPreferences sp);

	void clearData();

}
