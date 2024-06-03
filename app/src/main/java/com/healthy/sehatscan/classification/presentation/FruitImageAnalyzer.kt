package com.healthy.sehatscan.classification.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.healthy.sehatscan.classification.domain.Classification
import com.healthy.sehatscan.classification.domain.FruitClassifier

class FruitImageAnalyzer(
    private val classifier: FruitClassifier,
    private val onResults: (List<Classification>) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(321, 321) // TODO

            val results = classifier.classify(bitmap, rotationDegrees)
            onResults(results)
        }
        frameSkipCounter++

        image.close()
    }
}