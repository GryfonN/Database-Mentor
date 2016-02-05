package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.quick;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.QuickCodeCallResult;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;


/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public interface QuickCardPresenter extends Presenter {

    String SERVICE_PATH_UPLOAD_SQL_SCRIPT = "UploadSqlScript";

    String MSG_NO_FILE = "Choose file !";
    String MSG_NO_SQL_FILE = "Name of file has to be *.sql!";

    public interface QuickCardView extends IsWidget {

        public void setPresenter(QuickCardPresenter quickCardPresenter);

        public void resetView();

        public void showCodeResult(QuickCodeCallResult result);

        public void showCodeSQLError(String errorMsg);

        public FormPanel getFormPanelForUploadSQLScript();

        public FileUpload getFileUploadForUploadSQLScript();

        public Button getSubmitButtonForUploadSQLScript();

        public void setUploadedSQLScript(String sqlCode);

        public void showUploadProgress(boolean isVisible);
    }

    public void runSQLCode(String code);

    public void setupUploadFormForUploadSQLScript();

    public void getUploadedSQLScriptFromSession();
}
