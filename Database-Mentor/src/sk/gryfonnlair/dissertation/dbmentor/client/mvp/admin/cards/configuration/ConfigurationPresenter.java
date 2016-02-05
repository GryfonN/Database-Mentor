package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.configuration;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 08.04.14
 * Time: 09:43
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigurationPresenter extends Presenter {

    public interface ConfigurationView extends IsWidget {

        public static final String MSG_NO_USER_NAME = "Fill User Name !";
        public static final String MSG_NO_USER_PASS = "Fill User Password !";
        public static final String MSG_NO_URL = "Fill Connection url address !";
        public static final String MSG_NO_DATABASE_NAME = "Fill database name for user !";

        void setPresenter(ConfigurationPresenter configurationPresenter);

        void resetView();

        void fillFormular(String userName, String userPass, String url, String database);

        /**
         * nastavy style na TextBoxe a ak msg!=null tak ju napise inak success
         *
         * @param errorMsg ak NULL tak je Success, inak error msg show
         */
        void showSaveResult(String errorMsg);

        void showSaveProgress(boolean isProgressVisible);

        void setVisibleSaveButton(boolean visible);
    }

    void getConfiguration();

    void saveConfiguration(String userName, String userPass, String url, String database);
}
