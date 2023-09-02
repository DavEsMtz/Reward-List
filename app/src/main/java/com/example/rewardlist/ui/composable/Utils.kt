@file:Suppress("UNCHECKED_CAST")

package com.example.rewardlist.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.toMutableStateMap

private fun <K, V> snapshotStateMapSaver() = Saver<SnapshotStateMap<K, V>, Any>(
    save = { state -> state.toList() },
    restore = { value ->
        (value as? List<Pair<K, V>>)?.toMutableStateMap() ?: mutableStateMapOf<K, V>()
    }
)

private fun <T> snapshotStateList() = Saver<SnapshotStateList<T>, Any>(
    save = { state -> state.toList() },
    restore = { value ->
        (value as? List<T>)?.toMutableStateList() ?: mutableStateListOf()
    }
)

@Composable
fun <K, V> rememberSavableSnapshotStateMap(init: () -> SnapshotStateMap<K, V>): SnapshotStateMap<K, V> =
    rememberSaveable(saver = snapshotStateMapSaver(), init = init)

@Composable
fun <T> rememberSavableSnapshotStateList(init: () -> SnapshotStateList<T>): SnapshotStateList<T> =
    rememberSaveable(saver = snapshotStateList(), init = init)