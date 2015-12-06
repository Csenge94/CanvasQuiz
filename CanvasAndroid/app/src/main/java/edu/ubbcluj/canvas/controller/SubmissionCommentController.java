package edu.ubbcluj.canvas.controller;

import edu.ubbcluj.canvas.util.listener.InformationListener;

public interface SubmissionCommentController {
	void setComment(String comment);
	
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);
}
