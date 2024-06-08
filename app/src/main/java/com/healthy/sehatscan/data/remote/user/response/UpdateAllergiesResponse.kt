package com.healthy.sehatscan.data.remote.user.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

data class UpdateAllergiesResponse(

	@field:SerializedName("data")
	val data: List<AllergyData>? = null,

	@field:SerializedName("meta")
	val meta: ResponseMetaData? = null
)

data class Fruit(

	@field:SerializedName("fruit_name")
	val fruitName: String? = null,

	@field:SerializedName("fruit_id")
	val fruitId: Int? = null
)

data class AllergyData(

	@field:SerializedName("fruit")
	val fruit: Fruit? = null,

	@field:SerializedName("allergy_id")
	val allergyId: Int? = null
)
