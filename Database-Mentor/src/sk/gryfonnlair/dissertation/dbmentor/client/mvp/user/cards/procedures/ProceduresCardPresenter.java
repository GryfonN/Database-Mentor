package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.procedures;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureCallResult;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 10:24
 * To change this template use File | Settings | File Templates.
 */
public interface ProceduresCardPresenter extends Presenter {

    public interface ProceduresCardView extends IsWidget {

        void clearProcedureList();

        public void generateProcedureList(String[] arrayOfProcedures);

        public void fillDetailList(Map<String, String> procedureDetailInfo);

        public void fillCodeArea(String procedureCode);

        public void fillRunForm(List<ProcedureArgInfo> list);

        public void showRunFormResult(ProcedureCallResult result);

        public void setPresenter(ProceduresCardPresenter proceduresCardPresenter);
    }

    public void getProcedureList();

    public void getProcedureDetails(String procedureName);

    public void getProcedureCode(String procedureName);

    public void getProcedureArguments(String procedureName);

    public void callProcedure(String procedureName, List<ProcedureArgInfo> arguments);
}
