package com.example.historyvn_project.common

import android.content.SharedPreferences
import com.example.historyvn_project.model.CityModel
import com.example.historyvn_project.model.CollectionModel

class Global {
    companion object {
        var base_url = "https://historyvn-api-project-production.up.railway.app/api"
        var pref: SharedPreferences? = null
        var selectGity = 0
        var selectCollection = 0
        var selectCaterogy = 0
        var selectObject = 0
        var mapMarker = ""
        var buf = 0
    }
}