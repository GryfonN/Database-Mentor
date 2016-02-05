package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.bundles;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Bundle;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/29/14
 * Time: 7:07 PM
 * PResuvanie aktivnych a neaktivnych bundles
 */
public interface BundlesPresenter extends Presenter {
    public interface BundlesView extends IsWidget {

        void setPresenter(BundlesPresenter bundlesPresenter);

        void resetView();

        public void showProgress(boolean isProgressVisible);

        void generateListOfBundles(List<Bundle> list);

        void showErrorMsg(String errorMsg);
    }

    void getAllBundles();

    void deleteBundle(int bundleId, boolean active, String bundleName);

    void changeBundleStatus(int bundleId, boolean active, String bundleName);
}
