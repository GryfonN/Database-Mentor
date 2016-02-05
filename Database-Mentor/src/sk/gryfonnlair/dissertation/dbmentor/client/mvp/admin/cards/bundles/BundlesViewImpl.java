package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import sk.gryfonnlair.dissertation.dbmentor.client.DatabaseMentor;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.BundleStatus;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Bundle;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/29/14
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class BundlesViewImpl extends Composite implements BundlesPresenter.BundlesView {
    interface BundlesViewImplUiBinder extends UiBinder<HTMLPanel, BundlesViewImpl> {
    }

    private static BundlesViewImplUiBinder ourUiBinder = GWT.create(BundlesViewImplUiBinder.class);

    private BundlesPresenter presenter;

    public BundlesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(BundlesPresenter bundlesPresenter) {
        this.presenter = bundlesPresenter;
    }

    @Override
    public void resetView() {
        bundlesPanel.clear();
        errorTextBox.setVisible(false);
        errorTextBox.setText("");
    }

    @Override
    public void showProgress(boolean isProgressVisible) {
        if (isProgressVisible) {
            DatabaseMentor.gryfProgressPopupPanel.center();
        } else {
            DatabaseMentor.gryfProgressPopupPanel.hide();
        }
    }

    @Override
    public void generateListOfBundles(List<Bundle> list) {
        bundlesPanel.clear();
        showProgress(false);
        if (list == null) { //error porieseny na presentery
        } else if (list.isEmpty()) {
            showErrorMsg("Empty bundle dir on server");
        } else {
            for (Bundle b : list) {
                final BundleStatus bs = new BundleStatus(b.getId(), b.getBundleName(), b.isActive());
                bs.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        BundlesViewImpl.this.presenter.deleteBundle(bs.getId(), bs.getValue(), bs.getName());
                        bs.removeFromParent();
                    }
                });
                bs.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<Boolean> event) {
                        bs.setValue(event.getValue());
                        BundlesViewImpl.this.presenter.changeBundleStatus(bs.getId(), event.getValue(), bs.getName());
                    }
                });
                bundlesPanel.add(bs);
            }
        }
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        RootPanel.get("error_div").add(new Label(errorMsg));
        errorTextBox.setText(errorMsg);
        errorTextBox.setVisible(true);
    }

    @UiField
    FlowPanel bundlesPanel;
    @UiField
    TextBox errorTextBox;

    interface MyStyle extends CssResource {
        @ClassName("cardBody-BundlesCard")
        String cardBodyBundlesCard();

        @ClassName("cardTitle-BundlesCard")
        String cardTitleBundlesCard();

        @ClassName("bundlesPanel-BundlesCard")
        String bundlesPanelBundlesCard();

        @ClassName("errorTextBox-BundlesCard")
        String errorTextBoxBundlesCard();
    }
}