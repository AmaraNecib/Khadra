package com.example.khadra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    
    private val _items = MutableStateFlow<List<String>>(emptyList())
    val items = _items.asStateFlow()

    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun addItem(item: String) {
        viewModelScope.launch {
            _items.value = _items.value + item
        }
    }

    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }


}