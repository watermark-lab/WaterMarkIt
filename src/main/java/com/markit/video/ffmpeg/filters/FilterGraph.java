package com.markit.video.ffmpeg.filters;

import com.markit.api.positioning.Coordinates;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Mutable builder for an ffmpeg {@code filter_complex} graph.
 * <p>
 * Watermark steps are chained by feeding each filter's output label into the
 * next filter's input. This class owns that label bookkeeping: it tracks the
 * current input/output labels, joins fragments with commas, and collects the
 * temporary overlay image files that ffmpeg consumes as extra inputs and that
 * must be cleaned up afterwards.
 * </p>
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class FilterGraph {

    private static final String VIDEO_INPUT_LABEL = "[0:v]";

    private final StringBuilder filter = new StringBuilder();
    private final List<File> overlayImages = new ArrayList<>();
    private String lastLabel = VIDEO_INPUT_LABEL;
    private int step = 0;
    private boolean empty = true;

    /**
     * @return the label feeding the next filter's input
     */
    public String inLabel() {
        return step == 0 ? VIDEO_INPUT_LABEL : lastLabel;
    }

    /**
     * @return the label produced by the next filter's output
     */
    public String outLabel() {
        return "[v" + (step + 1) + "]";
    }

    /**
     * Appends one filter fragment and advances the graph so the next fragment
     * chains onto this one.
     *
     * @param fragment renders the fragment from the current input and output labels
     */
    public void append(BiFunction<String, String, String> fragment) {
        String rendered = fragment.apply(inLabel(), outLabel());
        if (!empty) {
            filter.append(',');
        }
        filter.append(rendered);
        lastLabel = outLabel();
        step++;
        empty = false;
    }

    /**
     * Registers a temporary overlay image as an extra ffmpeg input and appends an {@code overlay}
     * filter that composites it at the given coordinates. Keeping the input registration and the
     * filter that references its index together guarantees they stay in sync.
     *
     * @param image the overlay image file to add as an input
     * @param coord the position at which to composite the image onto the video
     */
    public void appendOverlay(File image, Coordinates coord) {
        overlayImages.add(image);
        int inputIndex = overlayImages.size();
        append((in, out) -> String.format("%s[%d:v]overlay=x=%d:y=%d%s",
                in, inputIndex, coord.getX(), coord.getY(), out));
    }

    /**
     * @return the immutable result describing the assembled filter graph
     */
    public FilterResult toResult() {
        return new FilterResult(filter.toString(), lastLabel, new ArrayList<>(overlayImages));
    }
}
