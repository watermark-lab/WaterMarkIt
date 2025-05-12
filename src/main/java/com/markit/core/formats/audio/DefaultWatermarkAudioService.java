package com.markit.core.formats.audio;


import com.markit.core.BaseWatermarkService;

import java.io.File;

/**
 * @author Oleg Cheban
 * @since 1.3.3
 */
public class DefaultWatermarkAudioService extends BaseWatermarkService<DefaultWatermarkAudioService> implements WatermarkAudioService {

    public DefaultWatermarkAudioService(File file) {
    }

    @Override
    public WatermarkAudioService withAudio(File file) {
        return null;
    }

    @Override
    public WatermarkAudioService withText(String text) {
        return null;
    }

    @Override
    public WatermarkAudioService startWith(int second) {
        return null;
    }
}
