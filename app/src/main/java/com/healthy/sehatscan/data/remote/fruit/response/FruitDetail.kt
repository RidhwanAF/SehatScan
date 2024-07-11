package com.healthy.sehatscan.data.remote.fruit.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

data class GetFruitDetailResponse(
    @field:SerializedName("meta")
    val meta: ResponseMetaData? = null,

    @field:SerializedName("data")
    val data: List<FruitDetailItem>? = null
)

data class FruitDetailItem(
    @field:SerializedName("fruit_id")
    val fruitId: Int? = null,

    @field:SerializedName("fruit_name")
    val fruitName: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("fruit_nutritions")
    val listFruitNutrition: List<Nutrition>? = null
)

data class Nutrition(
    @field:SerializedName("nutrition_id")
    val nutritionId: Int? = null,

    @field:SerializedName("nutrition_name")
    val nutritionName: String? = null
)