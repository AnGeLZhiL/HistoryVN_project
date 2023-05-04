package com.example.historyvn_project.common

import android.content.SharedPreferences
import com.example.historyvn_project.model.CityModel

class Global {
    companion object {
        var base_url = "https://historyvn-api-project-production.up.railway.app/api"
        var pref: SharedPreferences? = null
        var cities = ArrayList<CityModel>()
        var selectGity = 0
    }
}