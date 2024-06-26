package com.healthy.sehatscan.data.remote.disease.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData
import kotlinx.parcelize.Parcelize

data class GetDiseaseResponse(

	@field:SerializedName("data")
	val data: List<DiseaseDataItem?>? = null,

	@field:SerializedName("meta")
	val meta: ResponseMetaData? = null
)

data class Drink(
	@field:SerializedName("drink_name")
	val drinkName: String? = null,
	@field:SerializedName("description")
	val description: String? = null,
	@field:SerializedName("drink_id")
	val drinkId: Int? = null,
	@field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem>? = null
)

@Parcelize
data class IngredientsItem(
	@field:SerializedName("fruit_id")
	val fruitId: Int? = null,
	@field:SerializedName("fruit_name")
	val fruitName: String? = null
): Parcelable

data class DiseaseRestrictionsItem(

	@field:SerializedName("disease_restriction_id")
	val diseaseRestrictionId: Int? = null,

	@field:SerializedName("drink")
	val drink: Drink? = null
)

data class DiseaseDataItem(

	@field:SerializedName("disease_restrictions")
	val diseaseRestrictions: List<DiseaseRestrictionsItem>? = null,

	@field:SerializedName("disease_id")
	val diseaseId: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("disease_name")
	val diseaseName: String? = null
)
