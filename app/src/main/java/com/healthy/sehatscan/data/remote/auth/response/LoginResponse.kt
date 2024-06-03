package com.healthy.sehatscan.data.remote.auth.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

class UserLogin {
	data class LoginRequestBody(
		val email: String,
		val password: String
	)

	data class LoginResponse(
		@field:SerializedName("data")
		val data: Any? = null,

		@field:SerializedName("meta")
		val meta: ResponseMetaData? = null
	)
}
