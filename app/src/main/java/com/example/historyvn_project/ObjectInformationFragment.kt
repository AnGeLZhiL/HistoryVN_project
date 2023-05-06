package com.example.historyvn_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.historyvn_project.databinding.FragmentObjectInformationBinding
import com.example.historyvn_project.databinding.FragmentObjectsBinding
import okhttp3.OkHttpClient

class ObjectInformationFragment : Fragment() {

    private lateinit var binding: FragmentObjectInformationBinding
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObjectInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}