package com.markit.core.formats.audio;


import java.io.File;

/**
 * The Watermark Service for applying watermarks to audio
 *
 * @author Oleg Cheban
 * @since 1.3.3
 */
public interface WatermarkAudioService {
    WatermarkAudioService withAudio(File file);
    WatermarkAudioService withText(String text);
    WatermarkAudioService startWith(int second);
//    WatermarkAudioService and();
//    File apply(File file);
}
