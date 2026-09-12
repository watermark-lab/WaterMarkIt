package com.markit.audio.ffmpeg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Owns one operation's temporary directory and deletes it deterministically. */
public final class TemporaryFiles implements AutoCloseable {

    private final Path directory;

    public TemporaryFiles(String prefix) throws IOException {
        directory = Files.createTempDirectory(prefix);
    }

    public Path resolve(String fileName) {
        return directory.resolve(fileName);
    }

    public Path write(String fileName, byte[] bytes) throws IOException {
        Path file = resolve(fileName);
        Files.write(file, bytes);
        return file;
    }

    @Override
    public void close() throws IOException {
        if (!Files.exists(directory)) {
            return;
        }
        IOException failure = null;
        List<Path> pathsToDelete;
        try (Stream<Path> paths = Files.walk(directory)) {
            pathsToDelete = paths.sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        }
        for (Path path : pathsToDelete) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                if (failure == null) {
                    failure = e;
                } else {
                    failure.addSuppressed(e);
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
    }
}
