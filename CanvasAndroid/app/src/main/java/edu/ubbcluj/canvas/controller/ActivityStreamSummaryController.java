package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;

import edu.ubbcluj.canvas.model.ActivityStreamSummary;

public interface ActivityStreamSummaryController {

    List<ActivityStreamSummary> getData();

    void setSharedPreferences(SharedPreferences sp);

    void clearData();
}
