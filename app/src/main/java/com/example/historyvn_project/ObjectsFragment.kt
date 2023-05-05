package com.example.historyvn_project

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.historyvn_project.adapter.CategoryAdapter
import com.example.historyvn_project.adapter.ObjectsAdapter
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentObjectsBinding
import com.example.historyvn_project.model.CategoryModel
import com.example.historyvn_project.model.ObjectModel
import okhttp3.*
import okio.IOException
import org.json.JSONArray

class ObjectsFragment : Fragment(), ObjectsAdapter.Listner {

    private lateinit var binding: FragmentObjectsBinding
    private lateinit var objectsAdapter: ObjectsAdapter
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var objectsList: ArrayList<ObjectModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .url("${Global.base_url}/objects/${Global.selectCaterogy}")
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
                    objectsList = ArrayList()
                    for (i in 0 until json.length()){
                        objectsList.add(
                            ObjectModel(
                                id = json.getJSONObject(i).getInt("id_object"),
                                name = json.getJSONObject(i).getString("name"),
                                year = json.getJSONObject(i).getInt("year"),
                                location = json.getJSONObject(i).getString("location"),
                                description = json.getJSONObject(i).getString("description"),
                                map_marker = json.getJSONObject(i).getString("map_marker"),
                                image = json.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("image_url")
                            ))
                    }
                    Handler(Looper.getMainLooper()).post{
                        objectsAdapter = ObjectsAdapter(objectsList, this@ObjectsFragment)
                        binding.collectionsRecyclerView.adapter = objectsAdapter
                    }
                }
            }

        })
    }

    override fun onClickObject(objectModel: ObjectModel) {
        TODO("Not yet implemented")
    }
}