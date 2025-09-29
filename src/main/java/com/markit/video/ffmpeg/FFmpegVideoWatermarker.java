package com.markit.video.ffmpeg;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;
import com.markit.servicelocator.ServiceFactory;
import com.markit.video.VideoWatermarker;
import com.markit.video.ffmpeg.filters.FilterChainBuilder;
import com.markit.video.ffmpeg.filters.FilterResult;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class FFmpegVideoWatermarker implements VideoWatermarker {

    @Override
    public byte[] watermark(byte[] sourceVideoBytes, List<WatermarkAttributes> attrs) throws Exception {
        File input = Files.createTempFile("wmk-video-src", ".mp4").toFile();
        Files.write(input.toPath(), sourceVideoBytes);

        try {
            return watermark(input, attrs);
        } finally {
            input.delete();
        }
    }

    @Override
    public byte[] watermark(File file, List<WatermarkAttributes> attrs) throws Exception {
        var executor = (CommandExecutor) ServiceFactory.getInstance().getService(CommandExecutor.class);
        var filterChainBuilder = (FilterChainBuilder) ServiceFactory.getInstance().getService(FilterChainBuilder.class);

        FilterResult filterGraph = filterChainBuilder.build(file, attrs);
        return executor.execute(file, filterGraph);
    }

    @Override
    public int getPriority() {
        return Prioritizable.DEFAULT_PRIORITY;
    }
}
