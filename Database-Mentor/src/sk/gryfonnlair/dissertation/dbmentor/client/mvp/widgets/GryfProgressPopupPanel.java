package sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/21/14
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class GryfProgressPopupPanel extends PopupPanel {
    interface GryfProgressPopupPanelUiBinder extends UiBinder<Widget, GryfProgressPopupPanel> {
    }

    private static GryfProgressPopupPanelUiBinder ourUiBinder = GWT.create(GryfProgressPopupPanelUiBinder.class);

    interface Resources extends ClientBundle {
        @Source("GryfProgressPopupImage.GIF")
        public ImageResource gryfProgressPopupImage();
    }

    public GryfProgressPopupPanel() {

        setStyleName("");
        setModal(true);
        setGlassEnabled(true);

        centerPopupOnPage();

        add(ourUiBinder.createAndBindUi(this));

        Window.addResizeHandler(repositionOnResize);
    }

    /**
     * Handler kt spusti centrovanie na page
     */
    private ResizeHandler repositionOnResize = new ResizeHandler() {
        public void onResize(ResizeEvent event) {
            centerPopupOnPage();
        }
    };

    private void centerPopupOnPage() {
        if (this.isShowing()) {
            this.center();
        }
        //povodny kod na rucne pocitane centrovanie
//        int minOffset = 0; //px
//        int knownDialogWidth = 300; // this is in the CSS
//        int heightAboveCenter = 300; // will set the top to 200px above center
//
//        int left = Math.max(minOffset, (Window.getClientWidth() / 2) - (knownDialogWidth / 2));
//        int top = Math.max(minOffset, (Window.getClientHeight() / 2) - heightAboveCenter+100);
//        setPopupPosition(left, top);
    }
}