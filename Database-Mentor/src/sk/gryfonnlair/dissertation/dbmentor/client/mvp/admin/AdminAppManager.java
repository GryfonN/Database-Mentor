package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import sk.gryfonnlair.dissertation.dbmentor.client.di.MyGinjector;
import sk.gryfonnlair.dissertation.dbmentor.client.event.AdminSwitchCardEvent;
import sk.gryfonnlair.dissertation.dbmentor.client.event.AdminSwitchCardHandler;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.bundles.BundlesPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.configuration.ConfigurationPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload.UploadPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.header.AdminHeaderPresenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 1/8/14
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminAppManager implements Presenter, ValueChangeHandler<String> {

    private final AdminAppTemplate template;

    private final HandlerRegistration handlerRegistration;

    private final AdminHeaderPresenter adminHeaderPresenter;
    private final BundlesPresenter bundlesPresenter;
    private final UploadPresenter uploadPresenter;
    private final ConfigurationPresenter configurationPresenter;

    public AdminAppManager(MyGinjector myGinjector) {
        template = new AdminAppTemplate();
        EventBus eventBus = myGinjector.getEventBus();

        adminHeaderPresenter = myGinjector.getAdminHeaderPresenter();
        bundlesPresenter = myGinjector.getBundlesPresenter();
        uploadPresenter = myGinjector.getUploadPresenter();
        configurationPresenter = myGinjector.getConfigPresenter();

        handlerRegistration = eventBus.addHandler(AdminSwitchCardEvent.getType(), new AdminSwitchCardHandler() {
            @Override
            public void adminSwitchCard(AdminSwitchCardEvent event) {
                switch (event.getAdminCardType()) {
                    case UPLOAD:
                        displayUploadCard();
                        break;
                    case BUNDLES:
                        displayBundlesCard();
                        break;
                    case CONFIG:
                        displayConfigurationCard();
                        break;
                    default:
                        displayBundlesCard();
                        break;
                }
            }
        });
        bind();
    }

    /**
     * V presentery v tejto metode setujem viewvu presenter, vzajomna vezba, volat v konstruktore presentera
     */
    @Override
    public void bind() {
        template.setAdminAppPresenter(this);
    }

    /**
     * @param container HasWidgets kde vlozim svoj presenter/view, povinne riadky prve clear a potom add as widget
     */
    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(template.asWidget());

        adminHeaderPresenter.go(template.header);
        //DEFAULT
        uploadPresenter.go(template.content);
    }

    /**
     * Called when {@link com.google.gwt.event.logical.shared.ValueChangeEvent} is fired.
     *
     * @param event the {@link com.google.gwt.event.logical.shared.ValueChangeEvent} that was fired
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
    }

    public void removeHistoryHandler() {
        handlerRegistration.removeHandler();
    }

    private void displayBundlesCard() {
        bundlesPresenter.go(template.content);
    }

    private void displayUploadCard() {
        uploadPresenter.go(template.content);
    }

    private void displayConfigurationCard() {
        configurationPresenter.go(template.content);
    }
}
