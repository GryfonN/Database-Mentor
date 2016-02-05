package sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets;

import com.google.gwt.user.client.ui.Button;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/13/14
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebuggerSelectedRuleButton extends Button {

    private String ruleName;

    public String getRuleName() {
        return ruleName;
    }

    /**
     * ruleName setujem token pre neskorsi zber pravidiel aby som vedel o ake ide z buttonu
     *
     * @param ruleName
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
