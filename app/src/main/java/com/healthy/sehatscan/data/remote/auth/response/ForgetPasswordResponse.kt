package com.healthy.sehatscan.data.remote.auth.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

class UserForgetPassword {
    data class ForgetPasswordReqBody(
        val email: String
    )

    data class ForgetPasswordResponse(

        @field:SerializedName("data")
        val data: ForgetPasswordData? = null,

        @field:SerializedName("meta")
        val meta: ResponseMetaData? = null
    )

    data class ForgetPasswordData(

        @field:SerializedName("user_id")
        val userId: Int? = null,

        @field:SerializedName("role_id")
        val roleId: Int? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("email")
        val email: String? = null
    )
}