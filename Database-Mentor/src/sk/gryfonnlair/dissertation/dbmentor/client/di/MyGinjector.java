package sk.gryfonnlair.dissertation.dbmentor.client.di;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.bundles.BundlesPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.configuration.ConfigurationPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload.UploadPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.header.AdminHeaderPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.login.LoginPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.debugger.DebugCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.functions.FunctionsCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.procedures.ProceduresCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.quick.QuickCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.welcome.WelcomeCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.header.UserHeaderPresenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12/4/13
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */
@GinModules(GinjectorModule.class)
public interface MyGinjector extends Ginjector {

    EventBus getEventBus();

    //Views
    LoginPresenter.LoginView getLoginView();

    UserHeaderPresenter.UserHeaderView getUserHeaderView();

    WelcomeCardPresenter.WelcomeCardView getUserWelcomeCardView();

    QuickCardPresenter.QuickCardView getUserQuickCardView();

    ProceduresCardPresenter.ProceduresCardView getUserProceduresCardView();

    FunctionsCardPresenter.FunctionsCardView getUserFunctionsCardView();

    DebugCardPresenter.DebugCardView getUserDebuggerCardView();

    AdminHeaderPresenter.AdminHeaderView getAdminHeaderView();

    BundlesPresenter.BundlesView getBundlesView();

    UploadPresenter.UploadView getUploadView();

    ConfigurationPresenter.ConfigurationView getConfigView();

    //Presenters
    LoginPresenter getLoginPresenter();

    UserHeaderPresenter getUserHeaderPresenter();

    WelcomeCardPresenter getUserWelcomeCardPresenter();

    QuickCardPresenter getUserQuickCardPresenter();

    ProceduresCardPresenter getUserProceduresCardPresenter();

    FunctionsCardPresenter getUserFunctionsCardPresenter();

    DebugCardPresenter getUserDebuggerCardPresenter();

    AdminHeaderPresenter getAdminHeaderPresenter();

    BundlesPresenter getBundlesPresenter();

    UploadPresenter getUploadPresenter();

    ConfigurationPresenter getConfigPresenter();
}
