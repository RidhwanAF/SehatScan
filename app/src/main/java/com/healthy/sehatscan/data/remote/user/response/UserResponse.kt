package com.healthy.sehatscan.data.remote.user.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: UserData? = null
)

data class Role(

	@field:SerializedName("role_name")
	val roleName: String? = null,

	@field:SerializedName("role_id")
	val roleId: Int? = null
)

data class UserData(

	@field:SerializedName("allergies")
	val allergies: List<AllergiesItem?>? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("role")
	val role: Role? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("history_diseases")
	val historyDiseases: List<HistoryDiseasesItem?>? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class AllergiesItem(

	@field:SerializedName("fruits")
	val fruits: List<FruitsItem?>? = null,

	@field:SerializedName("allergy_id")
	val allergyId: Int? = null
)

data class HistoryDiseasesItem(

	@field:SerializedName("disease")
	val disease: Disease? = null,

	@field:SerializedName("history_disease_id")
	val historyDiseaseId: Int? = null
)

data class FruitsItem(

	@field:SerializedName("fruit_name")
	val fruitName: String? = null,

	@field:SerializedName("fruit_id")
	val fruitId: Int? = null
)

data class Disease(

	@field:SerializedName("disease_id")
	val diseaseId: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("disease_name")
	val diseaseName: String? = null
)
