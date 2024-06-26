package com.healthy.sehatscan.data.remote.drink.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData
import com.healthy.sehatscan.data.remote.disease.response.IngredientsItem
import kotlinx.parcelize.Parcelize

data class DrinkHistoryResponse(

	@field:SerializedName("data")
	val data: List<HistoryDataItem>? = null,

	@field:SerializedName("meta")
	val meta: ResponseMetaData? = null
)

data class HistoryDataItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("drinks")
	val drinks: List<Drink>? = null
)

@Parcelize
data class Drink(
	@field:SerializedName("drink_name")
	val drinkName: String? = null,
	@field:SerializedName("description")
	val description: String? = null,
	@field:SerializedName("drink_id")
	val drinkId: Int? = null,
	@field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem>? = null
): Parcelable
