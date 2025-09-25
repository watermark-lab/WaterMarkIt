package com.markit.video.ffmpeg.filters

import java.io.File

data class FilterStep(
    val filter: String,
    val lastLabel: String,
    val step: Int,
    val empty: Boolean,
    val tempImages: List<File> = emptyList()
) {
    constructor(filter: String, lastLabel: String, step: Int, first: Boolean) :
            this(filter, lastLabel, step, first, emptyList())
}