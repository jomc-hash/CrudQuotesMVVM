package dev.cardoso.quotesmvvm.data.model

data class QuoteResponse(
val success :Boolean,
val message :String ,
val data : List<QuoteModel>
)