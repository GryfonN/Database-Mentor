package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.configuration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.ConfigDefaultServiceAsync;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 08.04.14
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationPresenterImpl implements ConfigurationPresenter {

    private final ConfigurationView view;
    private final ConfigDefaultServiceAsync configDefaultServiceAsync;

    @Inject
    public ConfigurationPresenterImpl(ConfigurationView view, ConfigDefaultServiceAsync configDefaultServiceAsync) {
        this.view = view;
        this.configDefaultServiceAsync = configDefaultServiceAsync;
        bind();
    }

    @Override
    public void getConfiguration() {
        view.showSaveProgress(true);
        configDefaultServiceAsync.getDefaultConfig(new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("ConfigurationPresenterImpl.getConfiguration > onFailure");
                view.showSaveProgress(false);
                view.showSaveResult("Server could not get configuration EXP: " + caught.getMessage());
                view.setVisibleSaveButton(false);
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                GWT.log("ConfigurationPresenterImpl.getConfiguration > onSuccess");
                view.showSaveProgress(false);
                if (result != null && !result.isEmpty()) {
                    view.setVisibleSaveButton(true);
                    view.fillFormular(
                            result.get("user"),
                            result.get("pass"),
                            result.get("url"),
                            result.get("database")
                    );
                } else {
                    view.showSaveResult("Server could not get configuration : NULL or Empty");
                    view.setVisibleSaveButton(false);
                }
            }
        });
    }

    @Override
    public void saveConfiguration(String userName, String userPass, String url, String database) {
        configDefaultServiceAsync.saveDefaultConfig(
                userName, userPass, url, database,
                new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("ConfigurationPresenterImpl.saveConfiguration > onFailure");
                        view.showSaveResult(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        GWT.log("ConfigurationPresenterImpl.saveConfiguration > onSuccess");
                        view.showSaveResult(null);
                    }
                }
        );
    }

    /**
     * V presentery v tejto metode setujem viewvu presenter, vzajomna vezba, volat v konstruktore presentera
     */
    @Override
    public void bind() {
        view.setPresenter(this);
    }

    /**
     * @param container HasWidgets kde vlozim svoj presenter/view, povinne riadky prve clear a potom add as widget
     */
    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        view.resetView();
        //ak zapnem kartu getnem z serva config
        getConfiguration();
    }
}
