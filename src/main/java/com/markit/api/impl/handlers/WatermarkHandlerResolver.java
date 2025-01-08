package com.markit.api.impl.handlers;

import com.markit.api.FileType;

import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkHandlerResolver {
    public <T> WatermarkHandler getHandler(T file, FileType fileType, Executor executor){
        if (fileType.equals(FileType.PDF)){
            return new WatermarkPdfHandler().getHandler(file, executor);
        } else {
            return new WatermarkImagesHandler().getHandler(file, fileType);
        }
    }
}
