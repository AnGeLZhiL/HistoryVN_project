package com.example.historyvn_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentMapBinding
import com.example.historyvn_project.databinding.FragmentObjectInformationBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Global.buf == 0){
            MapKitFactory.setApiKey("fcd160cd-ed00-46d9-9c10-0002c4d876c8")
        }
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayMaps = Global.mapMarker.split(",").toTypedArray()

        val latitude = arrayMaps[0]
        val longitude = arrayMaps[1]

//        MapKitFactory.initialize(context)

        binding.mapView.map.move(
            CameraPosition(Point(latitude.toDouble(), longitude.toDouble()), 16.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 10f), null
        )

        var mapKit:MapKit = MapKitFactory.getInstance()


//        binding.mapView.map.mapObjects.addPlacemark(Point(latitude.toDouble(), longitude.toDouble()), )
//        binding.mapView.map.mapObjects.addPlacemark(latitude.toDouble(), longitude.toDouble()), )

        val view = View(requireContext()).apply {
            background = requireContext().getDrawable(R.drawable.ic_marker)
        }

        binding.mapView.map.mapObjects.addPlacemark(
            Point(latitude.toDouble(), longitude.toDouble()),
            ViewProvider(view)
        )

        Global.buf = 1
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }
}
