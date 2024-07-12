package com.pelagohealth.codingchallenge.presentation.utils

import org.junit.Test


class LimitedListTest {

    private val list = LimitedList<String>(3)
    @Test
    fun add() {
        list.add("1")
        list.add("2")
        list.add("3")
        list.add("4")
        assert(list.getItems().size == 3)
    }

    @Test
    fun remove() {
        list.add("1")
        list.add("2")
        list.add("3")
        list.remove(1)
        assert(list.getItems().size == 2)
    }

    @Test
    fun getItems() {
        list.add("1")
        list.add("2")
        list.add("3")
        list.add("4")
        assert(list.getItems().size == 3)
    }
}