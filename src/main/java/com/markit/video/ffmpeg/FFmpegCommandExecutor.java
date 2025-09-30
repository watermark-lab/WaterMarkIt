package com.markit.video.ffmpeg;

import com.markit.video.ffmpeg.filters.FilterResult;

import java.io.BufferedReader;
import java.io.File;
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

    @Override
    public byte[] execute(File input, FilterResult data) throws Exception {
        File output = Files.createTempFile("wmk-video-out", ".mp4").toFile();

        List<String> cmd = new ArrayList<>();
        cmd.add("ffmpeg");
        cmd.add("-y");
        cmd.add("-i");
        cmd.add(input.getAbsolutePath());

        for (File img : data.getTempImages()) {
            cmd.add("-i");
            cmd.add(img.getAbsolutePath());
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
        cmd.add("libx264");
        cmd.add("-preset");
        cmd.add("veryfast");
        cmd.add("-crf");
        cmd.add("23");
        cmd.add(output.getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            while (br.readLine() != null) { /* drain */ }
        }

        int code = p.waitFor();
        if (code != 0) throw new RuntimeException("ffmpeg failed with code " + code);

        byte[] bytes = Files.readAllBytes(output.toPath());

        output.delete();

        for (File img : data.getTempImages()) img.delete();

        return bytes;
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
