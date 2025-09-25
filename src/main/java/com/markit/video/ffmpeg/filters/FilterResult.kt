package com.markit.video.ffmpeg.filters

import java.io.File

data class FilterResult(
    val filter: String,
    val lastLabel: String,
    val tempImages: List<File> = emptyList()
)