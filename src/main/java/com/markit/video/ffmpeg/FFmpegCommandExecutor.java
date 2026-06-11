package com.markit.video.ffmpeg;

import com.markit.video.ffmpeg.filters.FilterResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class FFmpegCommandExecutor implements CommandExecutor {

    private static final String VIDEO_CODEC = "libx264";
    private static final String ENCODING_PRESET = "veryfast";
    private static final String CONSTANT_RATE_FACTOR = "23";

    @Override
    public byte[] execute(File input, FilterResult data) throws Exception {
        File output = Files.createTempFile("wmk-video-out", ".mp4").toFile();
        try {
            runFfmpeg(buildCommand(input, output, data));
            return Files.readAllBytes(output.toPath());
        } finally {
            output.delete();
            data.getTempImages().forEach(File::delete);
        }
    }

    private List<String> buildCommand(File input, File output, FilterResult data) {
        List<String> cmd = new ArrayList<>();
        cmd.add("ffmpeg");
        cmd.add("-y");
        cmd.add("-i");
        cmd.add(input.getAbsolutePath());

        for (File overlayImage : data.getTempImages()) {
            cmd.add("-i");
            cmd.add(overlayImage.getAbsolutePath());
        }

        if (!data.getFilter().isEmpty()) {
            cmd.add("-filter_complex");
            cmd.add(data.getFilter());
            cmd.add("-map");
            cmd.add(data.getLastLabel());
            cmd.add("-map");
            cmd.add("0:a?");
        }

        cmd.add("-c:v");
        cmd.add(VIDEO_CODEC);
        cmd.add("-preset");
        cmd.add(ENCODING_PRESET);
        cmd.add("-crf");
        cmd.add(CONSTANT_RATE_FACTOR);
        cmd.add(output.getAbsolutePath());
        return cmd;
    }

    private void runFfmpeg(List<String> command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        drainOutput(process);

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("ffmpeg failed with code " + exitCode);
        }
    }

    private void drainOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (reader.readLine() != null) { /* drain to keep the process from blocking */ }
        }
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
