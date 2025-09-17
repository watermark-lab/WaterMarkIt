package com.markit.pdf.overlay.opacity;

import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

public interface GraphicsStateManager extends Prioritizable {
    PDExtendedGraphicsState createOpacityState(int opacity);
}
