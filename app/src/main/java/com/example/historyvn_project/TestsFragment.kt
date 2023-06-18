package com.example.historyvn_project

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.historyvn_project.adapter.CityAdapter
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentInformationBinding
import com.example.historyvn_project.databinding.FragmentTestsBinding
import com.example.historyvn_project.model.CityModel
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class TestsFragment : Fragment(), CityAdapter.Listner {

    private lateinit var binding: FragmentTestsBinding
    private lateinit var cityAdapter: CityAdapter
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var cityList: ArrayList<CityModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestsBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .url("${Global.base_url}/cities")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    alertDialog
                        .setTitle("Ошибка подключения")
                        .setMessage("Проверьте подключение к интернету или попробуйте повторить ошибку позже")
                        .setCancelable(true)
                        .setPositiveButton("Ok") { dialog, it ->
                            dialog.cancel()
                        }.show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200){
                    val json = JSONArray(response.body.string())
                    cityList = ArrayList()
                    for (i in 0 until json.length()){
                        cityList.add(CityModel(
                            id = json.getJSONObject(i).getInt("id_city"),
                            name = json.getJSONObject(i).getString("name")
                        ))
                    }
                    Handler(Looper.getMainLooper()).post{
                        cityAdapter = CityAdapter(cityList, this@TestsFragment)
                        binding.citiesRecyclerView.adapter = cityAdapter
                    }
                }
            }

        })
    }

    private fun filterList(newText: String?) {
        if (newText != null){
            val filteredList = ArrayList<CityModel>()
            for (item in cityList){
                if (item.name.lowercase(Locale.ROOT).contains(newText)){
                    filteredList.add(item)
                }
            }

            if (filteredList.isEmpty()){
                binding.citiesRecyclerView.visibility = View.GONE
                binding.textNotFound.visibility = View.VISIBLE
            } else {
                cityAdapter.setFilteredList(filteredList)
                binding.citiesRecyclerView.visibility = View.VISIBLE
                binding.textNotFound.visibility = View.GONE
            }
        }
    }

    override fun onClickCity(cityModel: CityModel) {
        findNavController().navigate(R.id.action_testsFragment_to_collectionsTestsFragment)
        Global.selectTestGity = cityModel.id
        println("----------------------------${Global.selectTestGity}")
    }
}