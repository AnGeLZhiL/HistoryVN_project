package com.example.historyvn_project

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.models.SlideModel
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentObjectInformationBinding
import com.example.historyvn_project.databinding.FragmentObjectsBinding
import com.example.historyvn_project.model.ObjectModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import okhttp3.*
import okio.IOException
import org.json.JSONObject

class ObjectInformationFragment : Fragment() {

    private lateinit var binding: FragmentObjectInformationBinding
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private val imageList = ArrayList<SlideModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObjectInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .url("${Global.base_url}/object/${Global.selectObject}")
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
                    val json = JSONObject(response.body.string())
                    val jsonImageList = json.getJSONArray("images")

                    for (i in 0 until jsonImageList.length()){
//                        println("----------------${jsonImageList.getJSONObject(i).getString("image_url")}")
                        imageList.add(
                            SlideModel(imageUrl = jsonImageList.getJSONObject(i).getString("image_url"))
                        )
                    }

                    Handler(Looper.getMainLooper()).post {
                        binding.nameTextView.text = json.getString("name")
                        binding.yearTextView.text = json.getInt("year").toString()
                        binding.descriptionTextView.text = json.getString("description")
                        binding.locationTextView.text = json.getString("location")
                        Global.mapMarker = json.getString("map_marker")
                        binding.imageSlider.setImageList(imageList)
                    }
                }
            }

        })

        binding.clickShowDesc.setOnClickListener {
            if (binding.descriptionTextView.visibility == View.VISIBLE){
                binding.descriptionTextView.visibility = View.GONE
            } else {
                binding.descriptionTextView.visibility = View.VISIBLE
            }
        }

        binding.maps.setOnClickListener {
            findNavController().navigate(R.id.action_objectInformationFragment_to_mapFragment)
        }

//        imageList.add(SlideModel("https://img1.goodfon.ru/wallpaper/nbig/a/69/kartinka-3d-dikaya-koshka.jpg", "test1"))
//        imageList.add(SlideModel("https://img4.goodfon.ru/wallpaper/nbig/1/7c/kartinka-loshadi-liubov-serdechki.jpg"))


    }
}