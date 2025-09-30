package com.markit.video

import com.markit.utils.FileUtils
import com.markit.api.positioning.WatermarkPosition
import com.markit.api.WatermarkService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.IOException
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class VideoWatermarkingTest {

    @Test
    //@Disabled //todo configure ffmpeg at github actions
    @Throws(IOException::class)
    fun `given video when apply several watermarks then make watermarked vidio`() {
        val result = WatermarkService.create()
            .watermarkVideo(FileUtils.readFileFromClasspathAsBytes("video.mp4"))
                .withText("WaterMarkIt").color(Color.RED).end()
                .opacity(50)
                .position(WatermarkPosition.CENTER).end()
                .size(30)
            .and()
                .withImage(FileUtils.readFileFromClasspathAsBytes("logo.png"))
                .position(WatermarkPosition.BOTTOM_RIGHT).end()
                .size(8)
            .and()
                .withText("WaterMarkIt").end()
                .position(WatermarkPosition.BOTTOM_LEFT).end()
                .size(40)
            .and()
                .withImage(FileUtils.readFileFromClasspathAsBytes("logo.png"))
                .position(WatermarkPosition.TOP_LEFT).end()
                .size(8)
            .apply();

        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //FileUtils.outputFile(result, "video_watermark.mp4")
    }
}
