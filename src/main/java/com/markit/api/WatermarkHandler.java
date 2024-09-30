package com.markit.api;

import java.io.IOException;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@FunctionalInterface
public interface WatermarkHandler {
    byte[] apply() throws IOException;
}
