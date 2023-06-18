package com.example.historyvn_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.historyvn_project.R
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.BoxBinding
import com.example.historyvn_project.databinding.CityItemBinding
import com.example.historyvn_project.model.CityModel
import com.example.historyvn_project.model.CollectionModel
import com.squareup.picasso.Picasso

class CityAdapter (var cityList: ArrayList<CityModel>, val listner: Listner) : RecyclerView.Adapter<CityAdapter.CityViewHolder>(){

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CityItemBinding.bind(itemView)

        fun bind(cityModel: CityModel, listner: Listner) = with(binding) {
            categoryName.text = cityModel.name
//            Picasso.get().load(cityModel.image).into(categoryImg)

            itemView.setOnClickListener {
                listner.onClickCity(cityModel)
            }
        }
    }

    fun setFilteredList(cityList: ArrayList<CityModel>){
        this.cityList = cityList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false))
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cityList[position], listner)
    }

    override fun getItemCount() : Int {
        return cityList.size
    }

    interface Listner {
        fun onClickCity (cityModel: CityModel)
    }
}