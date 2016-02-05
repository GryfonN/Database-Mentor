package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/1/14
 * Time: 7:53 AM
 * To change this template use File | Settings | File Templates.
 */
public interface UploadPresenter extends Presenter {

    public String SERVICE_PATH_UPLOAD_BUNDLE = "UploadBundleFiles";
    public String MSG_RESULT_UPLOAD_SUCCESS = "Success";
    public String MSG_NO_BUNDLE_NAME = "No Bundle name";
    public String MSG_NO_FILE_DRIVER = "Choose driver JAR file";
    public String MSG_NO_FILE_MODULE = "Choose module JAR file";
    public String MSG_NO_MCL_CLASS_FULL_NAME = "No MCL class full name";

    public interface UploadView extends IsWidget {

        void setPresenter(UploadPresenter uploadPresenter);

        void resetView();

        void setUploadDriverDone(boolean done);

        void setUploadModuleDone(boolean done);

        /**
         * nastavy style na TextBoxe a ak msg!=null tak ju napise inak success
         *
         * @param errorMsg ak NULL tak je Success, inak error msg show
         */
        void showUploadResult(String errorMsg);

        /**
         * hide button show progreess
         *
         * @param isProgressVisible
         */
        void showUploadProgressForBundle(boolean isProgressVisible);

        //DRIVER
        FormPanel getFormPanelForDriver();

        FileUpload getFileUploadForDriver();

        Button getSubmitButtonForDriver();

        void showUploadProgressForDriver(boolean isProgressVisible);

        //MODULE
        FormPanel getFormPanelForModule();

        FileUpload getFileUploadForModule();

        Button getSubmitButtonForModule();

        void showUploadProgressForModule(boolean isProgressVisible);

    }

    void uploadBundle(String bundleName, String mclClassFullName);

    void setupUploadFormForDriver();

    void setupUploadFormForModule();
}
