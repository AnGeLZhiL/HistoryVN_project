package com.example.historyvn_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.historyvn_project.R
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.ObjectItemBinding
import com.example.historyvn_project.model.CollectionModel
import com.example.historyvn_project.model.ObjectModel
import com.squareup.picasso.Picasso

class ObjectsAdapter(var objectsList: ArrayList<ObjectModel>, val listner: ObjectsAdapter.Listner)
    : RecyclerView.Adapter<ObjectsAdapter.ObjectsViewHolder>() {

    interface Listner {
        fun onClickObject (objectModel: ObjectModel)
    }

    class ObjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ObjectItemBinding.bind(itemView)

        fun bind(objectModel: ObjectModel, listner: Listner) = with(binding) {
            nameTextView.text = objectModel.name
            if (objectModel.year == 1){
                yearTextView.visibility == View.GONE
            } else {
                yearTextView.text = objectModel.year.toString()
            }
            Picasso.get().load(objectModel.image).into(categoryImg)

            itemView.setOnClickListener {
                listner.onClickObject(objectModel)
            }
        }
    }

    fun setFilteredList(objectsList: ArrayList<ObjectModel>){
        this.objectsList = objectsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectsViewHolder {
        return ObjectsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.object_item, parent, false))
    }

    override fun onBindViewHolder(holder: ObjectsViewHolder, position: Int) {
        holder.bind(objectsList[position], listner)
    }

    override fun getItemCount(): Int = objectsList.size

}