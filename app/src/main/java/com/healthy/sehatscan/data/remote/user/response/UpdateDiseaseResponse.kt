package com.healthy.sehatscan.data.remote.user.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

data class UpdateDiseaseResponse(

	@field:SerializedName("data")
	val data: List<DiseaseDataItem?>? = null,

	@field:SerializedName("meta")
	val meta: ResponseMetaData? = null
)

data class DiseaseDataItem(

	@field:SerializedName("disease")
	val disease: DiseaseData? = null,

	@field:SerializedName("history_disease_id")
	val historyDiseaseId: Int? = null
)

data class DiseaseData(

	@field:SerializedName("disease_id")
	val diseaseId: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("disease_name")
	val diseaseName: String? = null
)
