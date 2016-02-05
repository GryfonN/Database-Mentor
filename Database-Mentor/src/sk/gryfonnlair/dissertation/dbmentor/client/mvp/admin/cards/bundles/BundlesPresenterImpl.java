package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Bundle;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.BundlesServiceAsync;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/29/14
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class BundlesPresenterImpl implements BundlesPresenter {

    private final BundlesView view;
    private final BundlesServiceAsync bundlesServiceAsync;

    @Inject
    public BundlesPresenterImpl(BundlesView view, BundlesServiceAsync bundlesServiceAsync) {
        this.view = view;
        this.bundlesServiceAsync = bundlesServiceAsync;
        bind();
    }

    /**
     * V presentery v tejto metode setujem viewvu presenter, vzajomna vezba, volat v konstruktore presentera
     */
    @Override
    public void bind() {
        view.setPresenter(this);
    }

    /**
     * @param container HasWidgets kde vlozim svoj presenter/view, povinne riadky prve clear a potom add as widget
     */
    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        view.resetView();
        getAllBundles();
    }

    @Override
    public void getAllBundles() {
        view.showProgress(true);
        bundlesServiceAsync.getAllBundles(new AsyncCallback<List<Bundle>>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("BundlesPresenterImpl.getAllBundles > onFailure EXP: " + caught.getMessage());
                view.showErrorMsg("BundlesPresenterImpl.getAllBundles > onFailure EXP: " + caught.getMessage());
                view.generateListOfBundles(null);
            }

            @Override
            public void onSuccess(List<Bundle> result) {
                GWT.log("BundlesPresenterImpl.getAllBundles > onSuccess ");
                view.generateListOfBundles(result);
            }
        });
    }

    @Override
    public void deleteBundle(int bundleId, boolean active, String bundleName) {
        bundlesServiceAsync.deleteBundle(bundleId, active, bundleName,
                new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("BundlesPresenterImpl.deleteBundle > deleteBundle EXP: " + caught.getMessage());
                        view.showErrorMsg("BundlesPresenterImpl.deleteBundle > deleteBundle EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        GWT.log("BundlesPresenterImpl.deleteBundle > onSuccess ");
                    }
                });
    }

    @Override
    public void changeBundleStatus(int bundleId, boolean active, String bundleName) {
        bundlesServiceAsync.moveBundle(bundleId, active, bundleName,
                new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("BundlesPresenterImpl.changeBundleStatus > deleteBundle EXP: " + caught.getMessage());
                        view.showErrorMsg("BundlesPresenterImpl.changeBundleStatus > deleteBundle EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        GWT.log("BundlesPresenterImpl.changeBundleStatus > onSuccess ");
                    }
                });
    }
}
