package sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 08.04.14
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class BundleStatus extends Composite implements HasClickHandlers, HasValue<Boolean> {
    interface BundleStatusUiBinder extends UiBinder<HTMLPanel, BundleStatus> {

    }
    private static BundleStatusUiBinder ourUiBinder = GWT.create(BundleStatusUiBinder.class);

    private int id;

    private String name;
    private boolean active;
    public BundleStatus(int bundleId, String bundleName, boolean active) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.id = bundleId;
        this.name = bundleName;
        this.active = active;

        this.bundleName.setText(bundleName);
        this.bundleToggleButton.setDown(active);
//        bundleToggleButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//            @Override
//            public void onValueChange(ValueChangeEvent<Boolean> event) {
//                BundleStatus.this.active = event.getValue();
//                GWT.log(BundleStatus.this.name + "=" + BundleStatus.this.active
//                        + " down=" + BundleStatus.this.bundleToggleButton.isDown());
//            }
//        });
    }

    /**
     * Gets this object's value.
     *
     * @return the object's value
     */
    @Override
    public Boolean getValue() {
        return active;
    }

    /**
     * Sets this object's value without firing any events. This should be
     * identical to calling setValue(value, false).
     * <p/>
     * It is acceptable to fail assertions or throw (documented) unchecked
     * exceptions in response to bad values.
     * <p/>
     * Widgets must accept null as a valid value. By convention, setting a widget to
     * null clears value, calling getValue() on a cleared widget returns null. Widgets
     * that can not be cleared (e.g. {@link com.google.gwt.user.client.ui.CheckBox}) must find another valid meaning
     * for null input.
     *
     * @param value the object's new value
     */
    @Override
    public void setValue(Boolean value) {
        this.active = value;
    }

    /**
     * Sets this object's value. Fires
     * {@link com.google.gwt.event.logical.shared.ValueChangeEvent} when
     * fireEvents is true and the new value does not equal the existing value.
     * <p/>
     * It is acceptable to fail assertions or throw (documented) unchecked
     * exceptions in response to bad values.
     *
     * @param value      the object's new value
     * @param fireEvents fire events if true and value is new
     */
    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} handler.
     *
     * @param handler the handler
     * @return the registration for the event
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return this.bundleToggleButton.addValueChangeHandler(handler);
    }

    /**
     * Adds a {@link com.google.gwt.event.dom.client.ClickEvent} handler.
     *
     * @param handler the click handler
     * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return bundleDeleteButton.addClickHandler(handler);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @UiField
    ToggleButton bundleToggleButton;
    @UiField
    Label bundleName;
    @UiField
    Button bundleDeleteButton;

    interface MyStyle extends CssResource {

        @ClassName("toogleButton-BundleStatus")
        String toogleButtonBundleStatus();

        @ClassName("bundleNameLabel-BundleStatus")
        String bundleNameLabelBundleStatus();

        @ClassName("deleteButton-BundleStatus")
        String deleteButtonBundleStatus();

        @ClassName("bundleIcon-BundleStatus")
        String bundleIconBundleStatus();

        @ClassName("main-BundleStatus")
        String mainBundleStatus();
    }
}