package com.example.historyvn_project.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.historyvn_project.R
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.BoxBinding
import com.example.historyvn_project.databinding.CollectionCategoriesBinding
import com.example.historyvn_project.model.CityModel
import com.example.historyvn_project.model.CollectionModel
import com.squareup.picasso.Picasso

class CollectionAdapter(val collectionList: ArrayList<CollectionModel>, val listner: CollectionAdapter.Listner)
    : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>(){

    class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CollectionCategoriesBinding.bind(itemView)

        fun bind(collectionModel: CollectionModel, listner: Listner) = with(binding) {
            categoryName.text = collectionModel.name
            Picasso.get().load(collectionModel.image).into(categoryImg)

            itemView.setOnClickListener {
                listner.onClickCollection(collectionModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.collection_categories, parent, false))
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(collectionList[position], listner)
    }

    override fun getItemCount(): Int = collectionList.size

    interface Listner {
        fun onClickCollection (collectionModel: CollectionModel)
    }
}