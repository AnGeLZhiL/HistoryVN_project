package com.example.historyvn_project

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.historyvn_project.adapter.CityAdapter
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentInformationBinding
import com.example.historyvn_project.model.CityModel
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class InformationFragment : Fragment(), CityAdapter.Listner {

    private lateinit var binding: FragmentInformationBinding
    private val cityAdapter = CityAdapter(this)
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    for (i in 0 until json.length()){
                        Global.cities.add(CityModel(
                            id = json.getJSONObject(i).getInt("id_city"),
                            name = json.getJSONObject(i).getString("name"),
                            image = json.getJSONObject(i).getJSONObject("images").getString("image_url")
                        ))
                    }
                    Handler(Looper.getMainLooper()).post{
                        binding.citiesRecyclerView.adapter = cityAdapter
                    }
                }
            }

        })
    }

    override fun onClickCity(cityModel: CityModel) {
        TODO("Not yet implemented")
    }
}