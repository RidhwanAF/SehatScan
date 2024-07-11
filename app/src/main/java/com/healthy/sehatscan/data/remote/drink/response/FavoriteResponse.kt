package com.healthy.sehatscan.data.remote.drink.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData
import kotlinx.parcelize.Parcelize

class FavoriteDrink {
    // Get
    data class GetFavoriteResponse(
        @field:SerializedName("meta")
        val meta: ResponseMetaData? = null,
        @field:SerializedName("data")
        val data: List<FavoriteItem>? = null
    )

    data class FavoriteItem(
        @field:SerializedName("favorite_id")
        val favoriteId: Int,
        @field:SerializedName("createdAt")
        val createdAt: String? = null,
        @field:SerializedName("updatedAt")
        val updatedAt: String? = null,
        @field:SerializedName("drink")
        val drink: Drink? = null
    )

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
    ): Parcelable

    @Parcelize
    data class IngredientsItem(
        @field:SerializedName("fruit_id")
        val fruitId: Int? = null,
        @field:SerializedName("fruit_name")
        val fruitName: String? = null,
        @field:SerializedName("nutritions")
        val nutrition: List<NutritionItem>? = null
    ): Parcelable

    @Parcelize
    data class NutritionItem(
        @field:SerializedName("nutrition_id")
        val nutritionId: Int? = null,
        @field:SerializedName("nutrition_name")
        val nutritionName: String? = null
    ): Parcelable

    // Add Remove
    data class AddRemoveResponse(

        @field:SerializedName("data")
        val data: Any? = null,

        @field:SerializedName("meta")
        val meta: ResponseMetaData? = null
    )

    data class AddRemoveFavoriteReqBody(
        @field:SerializedName("drink_id")
        val drinkId: Int
    )
}