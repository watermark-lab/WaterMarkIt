package com.markit.api;

import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@FunctionalInterface
public interface WatermarkProcessor {
    byte[] apply(List<WatermarkAttributes> watermarks) throws IOException;
}
