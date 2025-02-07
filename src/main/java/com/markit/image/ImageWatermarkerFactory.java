package com.markit.image;

import com.markit.servicelocator.DefaultServiceLocator;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Factory class for obtaining instances of {@link ImageWatermarker} implementations.
 *
 * @author Oleg Cheban
 * @since 1.3.2
 */
public class ImageWatermarkerFactory {
    private static final ImageWatermarkerFactory instance = new ImageWatermarkerFactory();
    private static final SortedSet<ImageWatermarker> services = new TreeSet<>((o1, o2) -> -1 * Integer.compare(o1.getPriority(), o2.getPriority()));

    public static ImageWatermarkerFactory getInstance() {
        return instance;
    }

    private ImageWatermarkerFactory() {
        try {
            for (ImageWatermarker lockService : DefaultServiceLocator.findInstances(ImageWatermarker.class)) {
                register(lockService);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void register(ImageWatermarker imageWatermarker) {
        services.add(imageWatermarker);
    }

    public ImageWatermarker getService() {
        try {
            return services.iterator().next().getClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
