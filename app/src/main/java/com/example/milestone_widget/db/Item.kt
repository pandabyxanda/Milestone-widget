package com.example.milestone_widget.db

data class Item(
    val id: Int,
    val name: String,
    val shortName: String,
    val description: String,
    val dateCreated: String,
    val actionCount: Int
)
