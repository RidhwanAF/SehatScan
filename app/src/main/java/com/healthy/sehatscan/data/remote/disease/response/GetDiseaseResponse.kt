package com.healthy.sehatscan.data.remote.disease.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

data class GetDiseaseResponse(
    @field:SerializedName("data")
    val data: List<DiseaseDataItem>? = null,

    @field:SerializedName("meta")
    val meta: ResponseMetaData? = null
)

data class DiseaseDataItem(
    @field:SerializedName("disease_id")
    val diseaseId: Int? = null,

    @field:SerializedName("disease_name")
    val diseaseName: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("disease_restrictions")
    val diseaseRestrictions: List<DiseaseRestrictionsItem>? = null
)

data class DiseaseRestrictionsItem(
    @field:SerializedName("disease_restriction_id")
    val diseaseRestrictionId: Int? = null,

    @field:SerializedName("nutrition")
    val nutrition: Nutrition? = null
)

data class Nutrition(
    @field:SerializedName("nutrition_id")
    val nutritionId: Int? = null,

    @field:SerializedName("nutrition_name")
    val nutritionName: String? = null
)
