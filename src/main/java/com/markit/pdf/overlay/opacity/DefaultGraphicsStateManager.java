package com.markit.pdf.overlay.opacity;

import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

public class DefaultGraphicsStateManager implements GraphicsStateManager {

    @Override
    public PDExtendedGraphicsState createOpacityState(int opacity) {
        var transparencyState = new PDExtendedGraphicsState();
        transparencyState.setNonStrokingAlphaConstant((float) (opacity / 100.0));
        return transparencyState;
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
