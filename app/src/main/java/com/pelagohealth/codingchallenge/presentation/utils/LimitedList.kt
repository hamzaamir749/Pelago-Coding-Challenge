package com.pelagohealth.codingchallenge.presentation.utils

class LimitedList<T>(private val limit: Int = 3) {
    private val items = mutableListOf<T>()

    fun add(item: T) {
        if (items.size >= limit) {
            items.removeAt(items.size - 1)
        }
        items.add(0, item)
    }

    fun remove(item: Int) {
        items.removeAt(item)
    }

    fun getItems(): List<T> {
        return items.toList()
    }
}