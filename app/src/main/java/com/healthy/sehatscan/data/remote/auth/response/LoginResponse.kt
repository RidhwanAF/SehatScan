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
		val data: LoginData? = null,

		@field:SerializedName("meta")
		val meta: ResponseMetaData? = null
	)

	data class User(

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("email")
		val email: String? = null
	)

	data class LoginData(

		@field:SerializedName("user")
		val user: User? = null,

		@field:SerializedName("token")
		val token: String? = null
	)
}
