package sk.gryfonnlair.dissertation.dbmentor.client.di;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.bundles.BundlesPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.bundles.BundlesPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.bundles.BundlesViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.configuration.ConfigurationPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.configuration.ConfigurationPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.configuration.ConfigurationViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload.UploadPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload.UploadPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload.UploadViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.header.AdminHeaderPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.header.AdminHeaderPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.header.AdminHeaderViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.login.LoginPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.login.LoginPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.login.LoginViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.debugger.DebugCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.debugger.DebugCardPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.debugger.DebugCardViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.functions.FunctionsCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.functions.FunctionsCardPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.functions.FunctionsCardViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.procedures.ProceduresCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.procedures.ProceduresCardPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.procedures.ProceduresCardViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.quick.QuickCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.quick.QuickCardPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.quick.QuickCardViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.welcome.WelcomeCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.welcome.WelcomeCardPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.welcome.WelcomeCardViewImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.header.UserHeaderPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.header.UserHeaderPresenterImpl;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.header.UserHeaderViewImpl;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12/4/13
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class GinjectorModule extends AbstractGinModule {
    @Override
    protected void configure() {

        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

        //Views
        bind(LoginPresenter.LoginView.class).to(LoginViewImpl.class).in(Singleton.class);

        bind(UserHeaderPresenter.UserHeaderView.class).to(UserHeaderViewImpl.class).in(Singleton.class);
        bind(WelcomeCardPresenter.WelcomeCardView.class).to(WelcomeCardViewImpl.class).in(Singleton.class);
        bind(QuickCardPresenter.QuickCardView.class).to(QuickCardViewImpl.class).in(Singleton.class);
        bind(ProceduresCardPresenter.ProceduresCardView.class).to(ProceduresCardViewImpl.class).in(Singleton.class);
        bind(FunctionsCardPresenter.FunctionsCardView.class).to(FunctionsCardViewImpl.class).in(Singleton.class);
        bind(DebugCardPresenter.DebugCardView.class).to(DebugCardViewImpl.class).in(Singleton.class);

        bind(AdminHeaderPresenter.AdminHeaderView.class).to(AdminHeaderViewImpl.class).in(Singleton.class);
        bind(BundlesPresenter.BundlesView.class).to(BundlesViewImpl.class).in(Singleton.class);
        bind(UploadPresenter.UploadView.class).to(UploadViewImpl.class).in(Singleton.class);
        bind(ConfigurationPresenter.ConfigurationView.class).to(ConfigurationViewImpl.class).in(Singleton.class);

        //Presenters
        bind(LoginPresenter.class).to(LoginPresenterImpl.class).in(Singleton.class);

        bind(UserHeaderPresenter.class).to(UserHeaderPresenterImpl.class).in(Singleton.class);
        bind(WelcomeCardPresenter.class).to(WelcomeCardPresenterImpl.class).in(Singleton.class);
        bind(QuickCardPresenter.class).to(QuickCardPresenterImpl.class).in(Singleton.class);
        bind(ProceduresCardPresenter.class).to(ProceduresCardPresenterImpl.class).in(Singleton.class);
        bind(FunctionsCardPresenter.class).to(FunctionsCardPresenterImpl.class).in(Singleton.class);
        bind(DebugCardPresenter.class).to(DebugCardPresenterImpl.class).in(Singleton.class);

        bind(AdminHeaderPresenter.class).to(AdminHeaderPresenterImpl.class).in(Singleton.class);
        bind(BundlesPresenter.class).to(BundlesPresenterImpl.class).in(Singleton.class);
        bind(UploadPresenter.class).to(UploadPresenterImpl.class).in(Singleton.class);
        bind(ConfigurationPresenter.class).to(ConfigurationPresenterImpl.class).in(Singleton.class);
    }
}
