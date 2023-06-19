package com.example.historyvn_project

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.historyvn_project.adapter.CityAdapter
import com.example.historyvn_project.adapter.CollectionAdapter
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentCollectionsBinding
import com.example.historyvn_project.databinding.FragmentInformationBinding
import com.example.historyvn_project.model.CityModel
import com.example.historyvn_project.model.CollectionModel
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class CollectionsFragment : Fragment(), CollectionAdapter.Listner{

    private lateinit var binding: FragmentCollectionsBinding
    private lateinit var collectionAdapter: CollectionAdapter
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var collectionList: ArrayList<CollectionModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
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
            .url("${Global.base_url}/collections/${Global.selectGity}")
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
                    collectionList = ArrayList()
                    for (i in 0 until json.length()){
                        collectionList.add(CollectionModel(
                            id = json.getJSONObject(i).getInt("id_collection"),
                            name = json.getJSONObject(i).getString("name"),
                            image = json.getJSONObject(i).getJSONObject("images").getString("image_url")
                        ))
                    }
                    Handler(Looper.getMainLooper()).post{
                        collectionAdapter = CollectionAdapter(collectionList, this@CollectionsFragment)
                        binding.collectionsRecyclerView.adapter = collectionAdapter
                    }
                }
            }

        })

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_collectionsFragment_to_informationFragment)
        }
    }

    private fun filterList(newText: String?) {
        if (newText != null){
            val filteredList = ArrayList<CollectionModel>()
            for (item in collectionList){
                if (item.name.lowercase(Locale.ROOT).contains(newText)){
                    filteredList.add(item)
                }
            }

            if (filteredList.isEmpty()){
                binding.collectionsRecyclerView.visibility = View.GONE
                binding.textNotFound.visibility = View.VISIBLE
            } else {
                collectionAdapter.setFilteredList(filteredList)
                binding.collectionsRecyclerView.visibility = View.VISIBLE
                binding.textNotFound.visibility = View.GONE
            }
        }
    }

    override fun onClickCollection(collectionModel: CollectionModel) {
        findNavController().navigate(R.id.action_collectionsFragment_to_categoriesFragment)
        Global.selectCollection = collectionModel.id
    }
}
