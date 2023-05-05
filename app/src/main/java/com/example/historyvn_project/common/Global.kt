package com.example.historyvn_project.common

import android.content.SharedPreferences
import com.example.historyvn_project.model.CityModel
import com.example.historyvn_project.model.CollectionModel

class Global {
    companion object {
        var base_url = "https://historyvn-api-project-production.up.railway.app/api"
        var pref: SharedPreferences? = null
        var cities = ArrayList<CityModel>()
        var collections = ArrayList<CollectionModel>()
        var selectGity = 0
        var signInCity = 0
        var signInCollection = 0
        var signInCategory = 0
    }
}