package com.healthy.sehatscan.classification.domain

import android.graphics.Bitmap

interface FruitClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}