package dev.cardoso.quotesmvvm.data.model

import com.google.gson.annotations.SerializedName

data class QuoteRequest (@SerializedName("quote") var quote:String
                         , @SerializedName("author") var author:String
                         , @SerializedName("id") var id:Int=0)
