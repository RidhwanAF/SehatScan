package com.healthy.sehatscan.data.remote.drink.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData
import kotlinx.parcelize.Parcelize

class HistoryDrink {
    data class DrinkHistoryResponse(

        @field:SerializedName("data")
        val data: List<HistoryDataItem>? = null,

        @field:SerializedName("meta")
        val meta: ResponseMetaData? = null
    )

    data class HistoryDataItem(
        @field:SerializedName("createdAt")
        val createdAt: String? = null,
        @field:SerializedName("fruit_name")
        val fruitName: String? = null,
        @field:SerializedName("drinks")
        val drinks: List<DrinkItem>? = null
    )

    @Parcelize
    data class DrinkItem(
        @field:SerializedName("drink")
        val drink: Drink? = null
    ) : Parcelable

    @Parcelize
    data class Drink(
        @field:SerializedName("drink_id")
        val drinkId: Int? = null,
        @field:SerializedName("drink_name")
        val drinkName: String? = null,
        @field:SerializedName("description")
        val description: String? = null,
        @field:SerializedName("ingredients")
        val ingredients: List<IngredientsItem>? = null
    ) : Parcelable

    @Parcelize
    data class IngredientsItem(
        @field:SerializedName("fruit_id")
        val fruitId: Int? = null,
        @field:SerializedName("fruit_name")
        val fruitName: String? = null,
        @field:SerializedName("nutritions")
        val listNutrition: List<NutritionItem>? = null
    ) : Parcelable

    @Parcelize
    data class NutritionItem(
        @field:SerializedName("nutrition_id")
        val nutritionId: Int? = null,
        @field:SerializedName("nutrition_name")
        val nutritionName: String? = null
    ) : Parcelable
}
