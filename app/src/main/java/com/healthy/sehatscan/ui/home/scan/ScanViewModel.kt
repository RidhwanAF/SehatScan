package com.healthy.sehatscan.ui.home.scan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.healthy.sehatscan.classification.domain.Classification
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private var authDataStore: AuthDataStore
) : ViewModel() {

    var scanResult by mutableStateOf<Classification?>(null)
        private set

    fun onScanResultChange(results: Classification) {
        scanResult = results
    }

}