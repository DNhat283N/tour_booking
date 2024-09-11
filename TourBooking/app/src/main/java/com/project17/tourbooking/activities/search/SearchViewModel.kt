package com.project17.tourbooking.activities.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.project17.tourbooking.utils.TourPackage

class SearchViewModel : ViewModel() {
    var historyItems = mutableStateListOf("All Tours")
        private set

    var inputValue = mutableStateOf("")
    var isSearched = mutableStateOf(false)

    private val allTours = mutableStateListOf<TourPackage>() // Holds all tours

    val tourList = mutableStateListOf<TourPackage>()

    fun updateTourList(tourPackages: List<TourPackage>) {
        allTours.clear()
        allTours.addAll(tourPackages)
        filterToursByName() // Initialize tourList with all tours
    }

    fun filterToursByName() {
        val searchText = inputValue.value.lowercase()
        if (searchText.isEmpty()) {
            // Show all tours when search input is empty
            tourList.clear()
            tourList.addAll(allTours)
        } else {
            // Filter tours based on the search text
            tourList.clear()
            tourList.addAll(allTours.filter { it.name.lowercase().contains(searchText) })
        }
    }

    fun addHistoryItem(historyContent: String){
        if(!historyItems.contains(historyContent)){
            historyItems.add(0, historyContent)
        }
    }

    fun deleteHistoryItem(index: Int){
        if(index in historyItems.indices){
            historyItems.removeAt(index)
        }
    }

    fun onBackButtonPress(navController: NavHostController){
        if(!isSearched.value){
            navController.popBackStack()
        }
        else{
            isSearched.value = false
            inputValue.value = ""
            filterToursByName() // Reset the tour list when search is cleared
        }
    }
}
