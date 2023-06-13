package com.example.historyvn_project.common

import android.content.SharedPreferences
import com.example.historyvn_project.model.CityModel
import com.example.historyvn_project.model.CollectionModel

class Global {
    companion object {
        var base_url = "http://angeli3q.beget.tech/public/api"
        var pref: SharedPreferences? = null
        var selectGity = 0
        var selectTestGity = 0
        var selectCollection = 0
        var selectTestCollection = 0
        var selectCaterogy = 0
        var selectTestCaterogy = 0
        var selectObject = 0
        var selectTestObject = 0
        var selectTest = 0
        var mapMarker = ""
        var buf = 0
        var countQuestionsObjectTest = 0
        var countQuestionsTest = 0
        var countCorrectObjectAnswer = 0
        var countCorrectAnswer = 0
        var testPro = 0
        var testPro2 = 0
    }
}