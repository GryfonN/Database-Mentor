package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import sk.gryfonnlair.dissertation.dbmentor.client.di.MyGinjector;
import sk.gryfonnlair.dissertation.dbmentor.client.event.UserSwitchCardEvent;
import sk.gryfonnlair.dissertation.dbmentor.client.event.UserSwitchCardHandler;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.debugger.DebugCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.functions.FunctionsCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.procedures.ProceduresCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.quick.QuickCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.welcome.WelcomeCardPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.header.UserHeaderPresenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 1/8/14
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserAppManager implements Presenter, ValueChangeHandler<String> {

    private final UserAppTemplate template;

    private final HandlerRegistration handlerRegistration;

    private final UserHeaderPresenter userHeaderPresenter;
    private final WelcomeCardPresenter welcomeCardPresenter;
    private final QuickCardPresenter quickCardPresenter;
    private final ProceduresCardPresenter proceduresCardPresenter;
    private final FunctionsCardPresenter functionsCardPresenter;
    private final DebugCardPresenter debugCardPresenter;

    public UserAppManager(MyGinjector myGinjector) {
        template = new UserAppTemplate();
        EventBus eventBus = myGinjector.getEventBus();

        userHeaderPresenter = myGinjector.getUserHeaderPresenter();
        welcomeCardPresenter = myGinjector.getUserWelcomeCardPresenter();
        quickCardPresenter = myGinjector.getUserQuickCardPresenter();
        proceduresCardPresenter = myGinjector.getUserProceduresCardPresenter();
        functionsCardPresenter = myGinjector.getUserFunctionsCardPresenter();
        debugCardPresenter = myGinjector.getUserDebuggerCardPresenter();

        handlerRegistration = eventBus.addHandler(UserSwitchCardEvent.getType(), new UserSwitchCardHandler() {
            @Override
            public void userSwitchCard(UserSwitchCardEvent userSwitchCardEvent) {
                switch (userSwitchCardEvent.getUserCardType()) {
                    case WELCOME:
                        displayWelcomeCard();
                        break;
                    case QUICK:
                        displayQuickCard();
                        break;
                    case PROCEDURES:
                        displayProceduresCard();
                        break;
                    case FUNCTIONS:
                        displayFunctionsCard();
                        break;
                    case DEBUGGER:
                        displayDebugCard();
                        break;
                    default:
                        displayWelcomeCard();
                        break;
                }
            }
        });

        bind();
    }

    @Override
    public void bind() {
        template.setUserAppPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(template.asWidget());

        userHeaderPresenter.go(template.header);
        //TODO FOR TEST  - welcome default
        welcomeCardPresenter.go(template.content);
//        debugCardPresenter.go(template.content);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
    }

    public void removeHistoryHandler() {
        handlerRegistration.removeHandler();
    }

    private void displayWelcomeCard() {
        welcomeCardPresenter.go(template.content);
    }

    private void displayQuickCard() {
        quickCardPresenter.go(template.content);
    }

    private void displayProceduresCard() {
        proceduresCardPresenter.go(template.content);
    }

    private void displayFunctionsCard() {
        functionsCardPresenter.go(template.content);
    }

    private void displayDebugCard() {
        debugCardPresenter.go(template.content);
    }

}
