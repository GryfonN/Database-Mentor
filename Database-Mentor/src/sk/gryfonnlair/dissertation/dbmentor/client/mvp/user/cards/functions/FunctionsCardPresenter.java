package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.functions;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.FunctionArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12.02.14
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public interface FunctionsCardPresenter extends Presenter {

    public interface FunctionsCardView extends IsWidget {

        void clearFunctionList();

        public void generateFunctionList(String[] arrayOfFunctions);

        public void fillDetailList(Map<String, String> functionDetailInfo);

        public void fillCodeArea(String functionCode);

        public void fillRunForm(List<FunctionArgInfo> list);

        public void showRunFormResult(String result);

        public void setPresenter(FunctionsCardPresenter functionsCardPresenter);
    }

    public void getFunctionList();

    public void getFunctionDetails(String functionName);

    public void getFunctionCode(String functionName);

    public void getFunctionArguments(String functionName);

    public void callFunction(String functionName, List<FunctionArgInfo> arguments);
}
