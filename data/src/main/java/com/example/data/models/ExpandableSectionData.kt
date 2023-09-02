package com.example.data.models

data class ExpandableSection<T>(
    val sectionTitle: String,
    val items: List<ExpandableItem<T>>
)

data class ExpandableItem<T>(
    val itemTitle: String,
    val item: T
)
