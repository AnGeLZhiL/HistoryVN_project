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
import com.example.historyvn_project.adapter.CityAdapter
import com.example.historyvn_project.adapter.TestObjectAdapter
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentObjectInformationBinding
import com.example.historyvn_project.databinding.FragmentTestsListBinding
import com.example.historyvn_project.model.CityModel
import com.example.historyvn_project.model.TestObjectModel
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class TestsListFragment : Fragment(), TestObjectAdapter.Listner {

    private lateinit var binding: FragmentTestsListBinding
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var testsList: ArrayList<TestObjectModel>
    private lateinit var testsAdapter: TestObjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .url("${Global.base_url}/testscat/${Global.selectTestCaterogy}")
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
                    testsList = ArrayList()
                    for (i in 0 until json.length()){
                        testsList.add(
                            TestObjectModel(
                                id = json.getJSONObject(i).getInt("id_test"),
                                name = json.getJSONObject(i).getString("name")
                        )
                        )
                    }
                    Handler(Looper.getMainLooper()).post{
                        testsAdapter = TestObjectAdapter(testsList, this@TestsListFragment)
                        binding.testsRecyclerView.adapter = testsAdapter
                    }
                }
            }

        })
    }

    override fun onClickTestObject(testObjectModel: TestObjectModel) {
        findNavController().navigate(R.id.action_testsListFragment_to_testInformationsFragment)
        Global.selectTest = testObjectModel.id
    }
}