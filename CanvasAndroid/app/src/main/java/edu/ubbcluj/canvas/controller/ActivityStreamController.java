package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;

import edu.ubbcluj.canvas.model.ActivityStream;
import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface ActivityStreamController {
    void addInformationListener(InformationListener il);

    void removeInformationListener(InformationListener il);

    List<ActivityStream> getData();

    void setSharedPreferences(SharedPreferences sp);

    void clearData();
}
