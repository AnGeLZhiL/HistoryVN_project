package com.example.historyvn_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.historyvn_project.R
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.CollectionCategoriesBinding
import com.example.historyvn_project.model.CategoryModel
import com.example.historyvn_project.model.CollectionModel
import com.squareup.picasso.Picasso

class CategoryAdapter(var categoryList: ArrayList<CategoryModel>, val listner: CategoryAdapter.Listner)
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CollectionCategoriesBinding.bind(itemView)

        fun bind(categoryModel: CategoryModel, listner: CategoryAdapter.Listner) = with(binding) {
            categoryName.text = categoryModel.name
//            Picasso.get().load(Global.url_image+categoryModel.image).into(categoryImg)
            Picasso.get().load(categoryModel.image).into(categoryImg)

            itemView.setOnClickListener {
                listner.onClickCategory(categoryModel)
            }
        }
    }

    fun setFilteredList(categoryList: ArrayList<CategoryModel>){
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.collection_categories, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position], listner)
    }

    override fun getItemCount(): Int =categoryList.size

    interface Listner {
        fun onClickCategory (categoryModel: CategoryModel)
    }

}