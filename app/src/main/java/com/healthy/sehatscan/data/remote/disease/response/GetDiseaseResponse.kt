package com.healthy.sehatscan.data.remote.disease.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

data class GetDiseaseResponse(

	@field:SerializedName("data")
	val data: List<DiseaseDataItem?>? = null,

	@field:SerializedName("meta")
	val meta: ResponseMetaData? = null
)

data class Fruit(

	@field:SerializedName("fruit_name")
	val fruitName: String? = null,

	@field:SerializedName("fruit_id")
	val fruitId: Int? = null
)

data class DiseaseRestrictionsItem(

	@field:SerializedName("disease_restriction_id")
	val diseaseRestrictionId: Int? = null,

	@field:SerializedName("fruit")
	val fruit: Fruit? = null
)

data class DiseaseDataItem(

	@field:SerializedName("disease_restrictions")
	val diseaseRestrictions: List<DiseaseRestrictionsItem?>? = null,

	@field:SerializedName("disease_id")
	val diseaseId: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("disease_name")
	val diseaseName: String? = null
)
