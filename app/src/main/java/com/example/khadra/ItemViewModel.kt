package com.example.khadra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    data class ListItem(val id: Int, val text: String)

    private val _items = MutableStateFlow<List<ListItem>>(emptyList())
    val items = _items.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var nextId = 0

    fun addItem(text: String) {
        viewModelScope.launch {
            _items.value = _items.value + ListItem(id = nextId++, text = text)
        }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            _items.value = _items.value.filter { it.id != id }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}