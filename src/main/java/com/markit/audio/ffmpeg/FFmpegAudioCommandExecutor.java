package com.markit.audio.ffmpeg;

import com.markit.servicelocator.Prioritizable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** ProcessBuilder-based FFmpeg executor with bounded diagnostics. */
public class FFmpegAudioCommandExecutor implements AudioCommandExecutor {

    private static final int DIAGNOSTIC_LIMIT = 16 * 1024;

    @Override
    public byte[] execute(File source, List<File> watermarkInputs, AudioFilterResult filter,
                          AudioOutputFormat outputFormat) throws Exception {
        Objects.requireNonNull(source, "Source audio is required");
        Objects.requireNonNull(watermarkInputs, "Watermark inputs are required");
        Objects.requireNonNull(filter, "Audio filter is required");
        Objects.requireNonNull(outputFormat, "Output format is required");

        try (TemporaryFiles temporaryFiles = new TemporaryFiles("wmk-audio-output-")) {
            Path output = temporaryFiles.resolve("result" + outputFormat.getExtension());
            List<String> command = buildCommand(source, watermarkInputs, filter, outputFormat, output);
            runFfmpeg(command);
            return Files.readAllBytes(output);
        }
    }

    List<String> buildCommand(File source, List<File> watermarkInputs, AudioFilterResult filter,
                              AudioOutputFormat outputFormat, Path output) {
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-hide_banner");
        command.add("-y");
        command.add("-i");
        command.add(source.getAbsolutePath());
        for (File watermark : watermarkInputs) {
            command.add("-i");
            command.add(watermark.getAbsolutePath());
        }
        command.add("-filter_complex");
        command.add(filter.getFilter());
        command.add("-map");
        command.add(filter.getOutputLabel());
        command.add("-vn");
        command.add("-c:a");
        command.add(outputFormat.getCodec());
        command.add(output.toAbsolutePath().toString());
        return command;
    }

    private void runFfmpeg(List<String> command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        DiagnosticCollector diagnostics = new DiagnosticCollector(process);
        Thread diagnosticThread = new Thread(diagnostics, "watermarkit-ffmpeg-diagnostics");
        diagnosticThread.setDaemon(true);
        diagnosticThread.start();
        try {
            int exitCode = process.waitFor();
            diagnosticThread.join();
            if (exitCode != 0) {
                throw new IOException("FFmpeg audio processing failed with exit code " + exitCode
                        + ". Diagnostic tail:\n" + diagnostics.getTail());
            }
            if (diagnostics.getFailure() != null) {
                throw new IOException("Unable to read FFmpeg diagnostics", diagnostics.getFailure());
            }
        } catch (InterruptedException e) {
            process.destroyForcibly();
            diagnosticThread.interrupt();
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    private static final class DiagnosticCollector implements Runnable {
        private final Process process;
        private final StringBuilder tail = new StringBuilder();
        private IOException failure;

        private DiagnosticCollector(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            char[] buffer = new char[2048];
            try (Reader reader = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)) {
                int count;
                while ((count = reader.read(buffer)) != -1) {
                    tail.append(buffer, 0, count);
                    if (tail.length() > DIAGNOSTIC_LIMIT) {
                        tail.delete(0, tail.length() - DIAGNOSTIC_LIMIT);
                    }
                }
            } catch (IOException e) {
                failure = e;
            }
        }

        private String getTail() {
            return tail.toString();
        }

        private IOException getFailure() {
            return failure;
        }
    }

    @Override
    public int getPriority() {
        return Prioritizable.DEFAULT_PRIORITY;
    }
}
