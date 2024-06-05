package com.healthy.sehatscan.data.remote.fruit.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

data class FruitResponse(
	val data: List<FruitItem?>? = null,
	val meta: ResponseMetaData? = null
)

data class FruitItem(
	val createdAt: String? = null,
	@field:SerializedName("fruit_name")
	val fruitName: String? = null,
	@field:SerializedName("fruit_id")
	val fruitId: Int? = null,
	val updatedAt: String? = null
)

