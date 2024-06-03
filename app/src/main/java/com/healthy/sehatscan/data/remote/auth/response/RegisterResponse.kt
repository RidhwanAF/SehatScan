package com.healthy.sehatscan.data.remote.auth.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

class UserRegister {
    data class RegisterRequestBody(
        val name: String,
        val email: String,
        val password: String
    )

    data class RegisterResponse(

        @field:SerializedName("data")
        val data: RegisterData? = null,

        @field:SerializedName("meta")
        val meta: ResponseMetaData? = null
    )

    data class RegisterData(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("email")
        val email: String? = null
    )
}