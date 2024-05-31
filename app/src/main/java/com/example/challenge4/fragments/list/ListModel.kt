package com.example.challenge4.fragments.list

class ListModel {
    private var data: Int = 1

    fun incrementData(): Int {
        return data++
    }

    fun getDataString(): String {
        return data.toString()
    }
}