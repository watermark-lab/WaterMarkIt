package com.markit.video;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;

import java.io.File;
import java.util.List;

/**
 * Interface for adding watermarks to videos.
 */
public interface VideoWatermarker extends Prioritizable {

    byte[] watermark(byte[] sourceVideoBytes, List<WatermarkAttributes> attrs) throws Exception;

    byte[] watermark(File file, List<WatermarkAttributes> attrs) throws Exception;

}