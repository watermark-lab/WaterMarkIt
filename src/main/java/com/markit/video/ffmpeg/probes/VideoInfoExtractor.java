package com.markit.video.ffmpeg.probes;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class VideoInfoExtractor {
    public static VideoDimensions getVideoDimensions(File videoFile) throws IOException, InterruptedException {
        Process process = getProcess(videoFile);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String output = reader.readLine();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("ffprobe failed with exit code: " + exitCode);
            }

            if (output != null && !output.trim().isEmpty()) {
                String[] dimensions = output.trim().split(",");
                if (dimensions.length == 2) {
                    int width = Integer.parseInt(dimensions[0]);
                    int height = Integer.parseInt(dimensions[1]);
                    return new VideoDimensions(width, height);
                }
            }

            throw new RuntimeException("Could not parse video dimensions from ffprobe output: " + output);
        }
    }

    @NotNull
    private static Process getProcess(File videoFile) throws IOException {
        if (!videoFile.exists()) {
            throw new IOException("Video file does not exist: " + videoFile.getAbsolutePath());
        }

        if (!videoFile.canRead()) {
            throw new IOException("Cannot read video file: " + videoFile.getAbsolutePath());
        }

        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe",
                "-v", "quiet",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height",
                "-of", "csv=p=0",
                videoFile.getAbsolutePath()
        );

        Process process = pb.start();
        return process;
    }
}
