package com.markit.api.formats.video;

import com.markit.api.WatermarkProcessor;
import com.markit.api.builders.DefaultVisualWatermarkBuilder;
import com.markit.exceptions.WatermarkingException;
import com.markit.servicelocator.ServiceFactory;
import com.markit.video.VideoWatermarker;

import java.io.File;

public final class DefaultWatermarkVideoBuilder
        extends DefaultVisualWatermarkBuilder<VideoWatermarkContentStep, WatermarkVideoBuilder>
        implements VideoWatermarkContentStep, WatermarkVideoBuilder {

    public DefaultWatermarkVideoBuilder(byte[] fileBytes) {
        super(createWatermarkProcessor(fileBytes));
    }

    public DefaultWatermarkVideoBuilder(File file) {
        super(createWatermarkProcessor(file));
    }

    private static WatermarkProcessor createWatermarkProcessor(File file) {
        return watermarks -> {
            try {
                return getVideoWatermarker().watermark(file, watermarks);
            } catch (Exception e) {
                throw new WatermarkingException("Error watermarking the video", e);
            }
        };
    }

    private static WatermarkProcessor createWatermarkProcessor(byte[] fileBytes) {
        return watermarks -> {
            try {
                return getVideoWatermarker().watermark(fileBytes, watermarks);
            } catch (Exception e) {
                throw new WatermarkingException("Error watermarking the video", e);
            }
        };
    }

    private static VideoWatermarker getVideoWatermarker() {
        return (VideoWatermarker) ServiceFactory.getInstance().getService(VideoWatermarker.class);
    }
}


