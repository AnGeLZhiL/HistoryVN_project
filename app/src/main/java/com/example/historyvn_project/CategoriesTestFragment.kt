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
import com.example.historyvn_project.adapter.CategoryAdapter
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentCategoriesBinding
import com.example.historyvn_project.databinding.FragmentCategoriesTestBinding
import com.example.historyvn_project.model.CategoryModel
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class CategoriesTestFragment : Fragment(), CategoryAdapter.Listner {

    private lateinit var binding: FragmentCategoriesTestBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var categoryList: ArrayList<CategoryModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .url("${Global.base_url}/categories/${Global.selectTestCollection}")
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
                    categoryList = ArrayList()
                    for (i in 0 until json.length()){
                        categoryList.add(
                            CategoryModel(
                                id = json.getJSONObject(i).getInt("id_category"),
                                name = json.getJSONObject(i).getString("name"),
                                image = json.getJSONObject(i).getJSONObject("images").getString("image_url")
                            )
                        )
                    }
                    Handler(Looper.getMainLooper()).post{
                        categoryAdapter = CategoryAdapter(categoryList, this@CategoriesTestFragment)
                        binding.collectionsRecyclerView.adapter = categoryAdapter
                    }
                }
            }

        })

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesTestFragment_to_collectionsTestsFragment)
        }
    }

    override fun onClickCategory(categoryModel: CategoryModel) {
        findNavController().navigate(R.id.action_categoriesTestFragment_to_testsListFragment)
        Global.selectTestCaterogy = categoryModel.id
    }
}