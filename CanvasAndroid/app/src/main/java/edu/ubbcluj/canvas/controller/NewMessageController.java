package edu.ubbcluj.canvas.controller;

import edu.ubbcluj.canvas.model.MessageSequence;
import edu.ubbcluj.canvas.util.listener.InformationListener;
import edu.ubbcluj.canvas.view.activity.MessageItemActivity;

public interface NewMessageController {

	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);
	
	public void setMessageItemActivity(MessageItemActivity activity);

	public void setBody(String body);

	public MessageSequence getData();
}
