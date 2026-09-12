package com.markit.api

import com.markit.api.formats.image.DefaultWatermarkImageBuilder
import com.markit.api.formats.image.ImageWatermarkContentStep
import com.markit.api.formats.image.WatermarkImageBuilder
import com.markit.api.formats.pdf.DefaultWatermarkPDFBuilder
import com.markit.api.formats.pdf.PdfWatermarkContentStep
import com.markit.api.formats.pdf.WatermarkPDFBuilder
import com.markit.api.formats.video.DefaultWatermarkVideoBuilder
import com.markit.api.formats.video.VideoWatermarkContentStep
import com.markit.api.formats.video.WatermarkVideoBuilder
import org.junit.jupiter.api.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FormatContentStepTest {

    @Test
    fun `concrete builders implement their public content steps`() {
        assertTrue(ImageWatermarkContentStep::class.java.isAssignableFrom(DefaultWatermarkImageBuilder::class.java))
        assertTrue(PdfWatermarkContentStep::class.java.isAssignableFrom(DefaultWatermarkPDFBuilder::class.java))
        assertTrue(VideoWatermarkContentStep::class.java.isAssignableFrom(DefaultWatermarkVideoBuilder::class.java))
    }

    @Test
    fun `format builder stages are top-level interfaces`() {
        assertNull(WatermarkImageBuilder::class.java.enclosingClass)
        assertNull(WatermarkPDFBuilder::class.java.enclosingClass)
        assertNull(WatermarkVideoBuilder::class.java.enclosingClass)
    }
}
