package com.example.policyagent.ui.listeners

import android.net.Uri

interface ImagePickerListener {
    fun onIMageSelected(image: String, mimeType: String?, fileUri: Uri)
}