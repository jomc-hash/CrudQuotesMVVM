package dev.cardoso.quotesmvvm.data.model
import dev.cardoso.quotesmvvm.data.local.entities.QuoteEntity
import com.google.gson.annotations.SerializedName


data class QuoteModel (
    @SerializedName("id") val id: Int,
    @SerializedName("quote") val quote: String,
    @SerializedName("author") val author: String,
){
    fun toQuoteEntity(): QuoteEntity {
        return QuoteEntity(id= this.id,
                quote = this.quote,
                author = this.author)
    }
}
