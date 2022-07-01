package com.example.workoutapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.databinding.ItemHistoryRowBinding

class HistoryAdapter(val allCompletedDatesList: ArrayList<String>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        val llHistoryMainItem = binding.llHistoryItemMain
        val tvItem = binding.tvItem
        val tvPosition = binding.tvPosition
    }

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return allCompletedDatesList.size
    }
    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val date : String = allCompletedDatesList[position]

        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = date
        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0){
            holder.llHistoryMainItem.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }else{
            holder.llHistoryMainItem.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }

}