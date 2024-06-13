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

    @field:SerializedName("drink_name")
    val drinkName: String? = null,

    @field:SerializedName("drink_id")
    val drinkId: Int? = null,

    @field:SerializedName("description")
    val description: String? = null
): Parcelable

data class DrinkRecommendReqBody(
    @field:SerializedName("fruit_name")
    val fruitName: String
)