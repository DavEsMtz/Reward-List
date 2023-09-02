package com.example.data.mappers

import com.example.data.models.ExpandableSection
import com.example.data.models.ExpandableItem
import com.example.domain.models.GroupedRewards


fun GroupedRewards.toExpandableSection() =
    ExpandableSection(
        sectionTitle = "Section: $listId (${rewards.size})",
        items = rewards.map { reward ->
            ExpandableItem(
                itemTitle = reward.name.orEmpty(),
                item = reward
            )
        }
    )
