package com.example.rewardlist.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.models.ExpandableItem
import com.example.data.models.ExpandableSection
import kotlinx.coroutines.launch

@Composable
fun <T> DrawExpandableListView(
    sections: List<ExpandableSection<T>>, itemClicked: (item: T) -> Unit = {}
) {
    ExpandableListView(sections = sections, itemClicked)
}


@Composable
private fun <T> ExpandableListView(
    sections: List<ExpandableSection<T>>, itemClicked: (item: T) -> Unit
) {
    val isExpandedMap = rememberSavableSnapshotStateMap {
        List(sections.size) { index -> index to false }.toMutableStateMap()
    }
    val showButton by remember {
        derivedStateOf { isExpandedMap.count { it.value } > 0 }
    }
    val scope = rememberCoroutineScope()

    Box {
        LazyColumn(content = {
            sections.onEachIndexed { index, sectionData ->
                section(sectionData = sectionData,
                    isExpanded = isExpandedMap[index] ?: false,
                    itemClicked = itemClicked,
                    onHeaderClick = {
                        isExpandedMap[index] = !(isExpandedMap[index] ?: false)
                    })
            }
        })

        AnimatedVisibility(
            visible = showButton,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
        ) {
            Button(
                onClick = {
                    scope.launch {
                        isExpandedMap.onEachIndexed { index, _ ->
                            isExpandedMap[index] = false
                        }
                    }
                },
            ) {
                Text(text = "Collapse All")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun <T> LazyListScope.section(
    sectionData: ExpandableSection<T>,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
    itemClicked: (item: T) -> Unit
) {
    stickyHeader {
        SectionHeader(
            text = sectionData.sectionTitle,
            isExpanded = isExpanded,
            onHeaderClicked = onHeaderClick
        )
    }

    if (isExpanded) {
        items(items = sectionData.items) { item ->
            SectionItem(item, onItemClick = itemClicked)
        }
    }
}

@Composable
fun SectionHeader(text: String, isExpanded: Boolean, onHeaderClicked: () -> Unit) {
    Column {
        Row(modifier = Modifier
            .clickable { onHeaderClicked() }
            .background(Color.LightGray)
            .padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            val rotation by animateFloatAsState(
                targetValue = if (isExpanded) 0f else -180f, animationSpec = tween(800)
            )

            Icon(
                Icons.Default.KeyboardArrowUp,
                modifier = Modifier.rotate(rotation),
                contentDescription = "",
            )
        }
        Divider(color = Color.Black, thickness = 1.dp)
    }
}

@Composable
fun <T> SectionItem(sectionItem: ExpandableItem<T>, onItemClick: (item: T) -> Unit) {
    Column(modifier = Modifier
        .clickable { onItemClick(sectionItem.item) }
    ) {
        Text(
            text = sectionItem.itemTitle,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        )
        Divider()
    }
}