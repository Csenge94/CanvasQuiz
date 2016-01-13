package edu.ubbcluj.canvas.controller;

import java.util.List;

import android.content.SharedPreferences;

import com.instructure.canvasapi.model.Quiz;

import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface QuizController {
    void addInformationListener(InformationListener il);

    void removeInformationListener(InformationListener il);

    List<Quiz> getData();

    void setSharedPreferences(SharedPreferences sp);

    void clearData();
}
