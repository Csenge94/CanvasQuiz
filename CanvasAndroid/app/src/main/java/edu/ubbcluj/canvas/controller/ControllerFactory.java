package edu.ubbcluj.canvas.controller;

import android.content.Context;

import edu.ubbcluj.canvas.controller.canvasAPI.CanvasAssignmentsController;
import edu.ubbcluj.canvas.controller.canvasAPI.CanvasCoursesController;
import edu.ubbcluj.canvas.controller.rest.RestApiControllerFactory;

public abstract class ControllerFactory {
	public static ControllerFactory getInstance() {
		return new RestApiControllerFactory();
	}	
	
	public abstract UserController getUserController();
	public abstract ActivityStreamController getDashboardController();
	public abstract ActivityStreamController getDashboardController2();
	public abstract ActivityStreamSummaryController getActivityStreamSummaryController();
	public abstract CoursesController getCoursesController();
	public abstract CanvasCoursesController getCoursesController2();
	public abstract AssignmentsController getAssignmentsController();
	public abstract CanvasAssignmentsController getAssignmentsController2();
	public abstract ToDoController getToDoController();
	public abstract AnnouncementController getAnnouncementController();
	public abstract AnnouncementCommentController getAnnouncementCommentController();
	public abstract ConversationController getConversationController();
	public abstract MessageSequenceController getMessageSequenceController();
	public abstract FolderController getFolderController();
	public abstract SubmissionCommentController getSubmissionCommentController();
	public abstract NewMessageController getNewMessageController();
	public abstract QuizController getQuizController();
}
