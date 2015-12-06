package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;

import edu.ubbcluj.canvas.model.Announcement;
import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface AnnouncementController {
    void addInformationListener(InformationListener il);

    void removeInformationListener(InformationListener il);

    List<Announcement> getData();

    void setSharedPreferences(SharedPreferences sp);

    void clearData();
}
