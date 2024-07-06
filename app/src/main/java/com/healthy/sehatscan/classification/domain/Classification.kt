package com.healthy.sehatscan.classification.domain

import android.graphics.Bitmap

data class Classification(
    val image: Bitmap,
    val name: String,
    val score: Float
)
