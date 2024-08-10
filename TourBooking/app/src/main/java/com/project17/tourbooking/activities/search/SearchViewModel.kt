package com.project17.tourbooking.activities.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.project17.tourbooking.navigates.NavigationItems

class SearchViewModel: ViewModel() {
    var historyItems = mutableStateListOf(
        "Example History Item 1",
        "Example History Item 2",
        "Example History Item 3"
    )
    private set

    var inputValue = mutableStateOf("")
    var isSearched = mutableStateOf(false)

    fun deleteHistoryItem(
        index: Int
    ){
        if(index in historyItems.indices){
            historyItems.removeAt(index)
        }
    }

    fun addHistoryItem(historyContent: String){
        historyItems.add(0, historyContent)
    }

    fun onBackButtonPress(navController: NavHostController){
        if(!isSearched.value){
            navController.popBackStack()
        }
        else{
            isSearched.value = false
            inputValue.value = ""
        }
    }

}