package com.markit.audio.ffmpeg;

import com.markit.servicelocator.Prioritizable;

import java.io.File;
import java.util.List;

/** Executes a prepared FFmpeg audio mixing operation. */
public interface AudioCommandExecutor extends Prioritizable {

    byte[] execute(File source, List<File> watermarkInputs, AudioFilterResult filter,
                   AudioOutputFormat outputFormat) throws Exception;
}
