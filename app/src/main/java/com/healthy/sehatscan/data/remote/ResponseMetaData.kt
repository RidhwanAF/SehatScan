package com.healthy.sehatscan.data.remote

import com.google.gson.annotations.SerializedName

data class ResponseMetaData(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
