package com.markit.video.ffmpeg.filters

import java.io.File

/**
 * ffmpeg input data (filters (drawtext and overlay), files of watermarks, and labels)
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
data class FilterResult(
    val filter: String,
    val lastLabel: String,
    val tempImages: List<File> = emptyList()
)