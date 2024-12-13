package com.markit.api.handlers;

import com.markit.api.FileType;
import com.markit.image.DefaultImageWatermarker;

import java.io.File;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkImagesHandler {
    public <T> WatermarkHandler getHandler(T file, FileType fileType) {
        var imageWatermarker = new DefaultImageWatermarker();
        if (file instanceof byte[]){
            return (watermarks) -> imageWatermarker.watermark((byte[]) file, fileType, watermarks);
        } else if (file instanceof File){
            return (watermarks) -> imageWatermarker.watermark((File) file, fileType, watermarks);
        }

        throw new IllegalArgumentException("Unsupported file type");
    }
}
