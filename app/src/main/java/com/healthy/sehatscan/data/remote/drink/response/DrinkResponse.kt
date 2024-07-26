package com.healthy.sehatscan.data.remote.drink.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData
import kotlinx.parcelize.Parcelize

data class DrinkResponse(

    @field:SerializedName("data")
    val data: List<DrinkItem>? = null,

    @field:SerializedName("meta")
    val meta: ResponseMetaData? = null
)

@Parcelize
data class DrinkItem(
    @field:SerializedName("drink_id")
    val drinkId: Int? = null,

    @field:SerializedName("drink_name")
    val drinkName: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("ingredients")
    val ingredients: List<Ingredient>? = null
) : Parcelable

@Parcelize
data class Ingredient(
    @field:SerializedName("fruit_id")
    val fruitId: Int? = null,

    @field:SerializedName("fruit_name")
    val fruitName: String? = null,

    @field:SerializedName("nutritions")
    val listNutrition: List<Nutrition>? = null
) : Parcelable

@Parcelize
data class Nutrition(
    @field:SerializedName("nutrition_id")
    val nutritionId: Int? = null,

    @field:SerializedName("nutrition_name")
    val nutritionName: String? = null
) : Parcelable

data class DrinkRecommendReqBody(
    @field:SerializedName("fruit_name")
    val fruitName: String
)