package sk.gryfonnlair.dissertation.dbmentor.client.mvp;

import com.google.gwt.user.client.ui.HasWidgets;

public interface Presenter {
    /**
     * V presentery v tejto metode setujem viewvu presenter, vzajomna vezba, volat v konstruktore presentera
     */
    public void bind();

    /**
     * @param container HasWidgets kde vlozim svoj presenter/view, povinne riadky prve clear a potom add as widget
     */
    public void go(final HasWidgets container);
}
