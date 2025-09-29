package com.markit.video.ffmpeg.filters

import java.io.File

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
data class FilterResult(
    val filter: String,
    val lastLabel: String,
    val tempImages: List<File> = emptyList()
)