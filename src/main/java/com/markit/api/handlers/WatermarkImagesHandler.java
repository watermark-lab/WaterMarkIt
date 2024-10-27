package com.markit.api.handlers;

import com.markit.api.FileType;
import com.markit.image.DefaultImageWatermarker;
import com.markit.image.ImageWatermarker;

import java.io.File;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkImagesHandler {
    private ImageWatermarker imageWatermarker;

    public <T> WatermarkHandler getHandler(T file, FileType fileType) {
        this.imageWatermarker = new DefaultImageWatermarker();
        if (file instanceof byte[]){
            return (watermarks) -> imageWatermarker.watermark((byte[]) file, fileType, watermarks);
        } else if (file instanceof File){
            return (watermarks) -> imageWatermarker.watermark((File) file, fileType, watermarks);
        }

        throw new IllegalArgumentException("Unsupported file type");
    }
}
