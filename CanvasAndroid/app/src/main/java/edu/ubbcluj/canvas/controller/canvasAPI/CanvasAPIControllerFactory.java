package edu.ubbcluj.canvas.controller.canvasAPI;

import edu.ubbcluj.canvas.controller.ActivityStreamSummaryController;
import edu.ubbcluj.canvas.controller.AnnouncementCommentController;
import edu.ubbcluj.canvas.controller.AnnouncementController;
import edu.ubbcluj.canvas.controller.AssignmentsController;
import edu.ubbcluj.canvas.controller.ControllerFactory;
import edu.ubbcluj.canvas.controller.ConversationController;
import edu.ubbcluj.canvas.controller.CoursesController;
import edu.ubbcluj.canvas.controller.FolderController;
import edu.ubbcluj.canvas.controller.MessageSequenceController;
import edu.ubbcluj.canvas.controller.NewMessageController;
import edu.ubbcluj.canvas.controller.SubmissionCommentController;
import edu.ubbcluj.canvas.controller.ToDoController;
import edu.ubbcluj.canvas.controller.rest.RestActivityStreamController;
import edu.ubbcluj.canvas.controller.rest.RestActivityStreamSummaryController;
import edu.ubbcluj.canvas.controller.rest.RestAnnouncementCommentController;
import edu.ubbcluj.canvas.controller.rest.RestAnnouncementController;
import edu.ubbcluj.canvas.controller.rest.RestAssignmentsController;
import edu.ubbcluj.canvas.controller.rest.RestConversationController;
import edu.ubbcluj.canvas.controller.rest.RestCoursesController;
import edu.ubbcluj.canvas.controller.rest.RestFolderController;
import edu.ubbcluj.canvas.controller.rest.RestMessageSequenceController;
import edu.ubbcluj.canvas.controller.rest.RestNewMessageController;
import edu.ubbcluj.canvas.controller.rest.RestSubmissionCommentController;
import edu.ubbcluj.canvas.controller.rest.RestToDoController;

/**
 * Created by Csenge on 29.12.2015.
 */
public class CanvasAPIControllerFactory {
/*
    @Override
    public edu.ubbcluj.canvas.controller.rest.RestUserController getUserController() {
        return new edu.ubbcluj.canvas.controller.rest.RestUserController();
    }

    @Override
    public RestActivityStreamController getDashboardController() {
        return new RestActivityStreamController();
    }
*/
//    @Override
    public CoursesController getCoursesController() {
        return new CanvasCoursesController();
    }
/*
    @Override
    public AssignmentsController getAssignmentsController() {
        return new RestAssignmentsController();
    }

    @Override
    public ToDoController getToDoController() {
        return new RestToDoController();
    }

    @Override
    public AnnouncementController getAnnouncementController() {
        return new RestAnnouncementController();
    }

    @Override
    public ConversationController getConversationController() {
        return new RestConversationController();
    }

    @Override
    public MessageSequenceController getMessageSequenceController() {
        return new RestMessageSequenceController();
    }

    @Override
    public FolderController getFolderController() {
        return new RestFolderController();
    }

    @Override
    public ActivityStreamSummaryController getActivityStreamSummaryController(){
        return new RestActivityStreamSummaryController();
    }

    @Override
    public AnnouncementCommentController getAnnouncementCommentController() {
        return new RestAnnouncementCommentController();
    }

    @Override
    public SubmissionCommentController getSubmissionCommentController() {
        return new RestSubmissionCommentController();
    }

    public NewMessageController getNewMessageController() {
        return new RestNewMessageController();

    }*/
}
