package dev.cardoso.quotesmvvm.data.model

import com.google.gson.annotations.SerializedName

data class AddQuoteResponse(
    @SerializedName("success") val success:Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: QuoteModel
)
