package dev.cardoso.quotesmvvm.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.databinding.QuoteItemBinding

class QuotesAdapter(val quoteList: List<QuoteModel>, private var optionsClickListener: OptionsClickListener) : RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: QuoteItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        private var currentQuote: QuoteModel? = null

    }


    interface OptionsClickListener {
        fun onUpdateQuote(quote: QuoteModel)
        fun onDeleteQuote(quote:QuoteModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(quoteList[position]){

                binding.tvItemQuote.setText(this.quote)
                binding.tvItemAuthor.setText(this.author)
                binding.btnItemEdit.setOnClickListener{
                    optionsClickListener.onUpdateQuote(this)
//                    Toast.makeText(it.context, this.author, Toast.LENGTH_SHORT).show()
                }
                binding.btnItemDelete.setOnClickListener{
                    optionsClickListener.onDeleteQuote(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return quoteList.size
    }

}
