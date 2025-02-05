package com.example.khadra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    // قائمة العناصر
    private val _items = MutableStateFlow<List<String>>(emptyList())
    val items = _items.asStateFlow()

    // استعلام البحث
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // إضافة عنصر جديد
    fun addItem(item: String) {
        viewModelScope.launch {
            _items.value = _items.value + item
        }
    }

    // تحديث استعلام البحث
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredItems(): List<String> {
        return if (_searchQuery.value.isEmpty()) {
            _items.value
        } else {
            _items.value.filter { it.contains(_searchQuery.value, ignoreCase = true) }
        }


    }
}