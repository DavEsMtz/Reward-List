package com.example.rewardlist.ui.list

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.data.mappers.toExpandableSection
import com.example.rewardlist.R
import com.example.rewardlist.ui.composable.DrawExpandableListView
import com.example.rewardlist.ui.composable.DrawInfoMessage
import com.example.rewardlist.ui.composable.DrawProgressIndicator


@Composable
fun ListScreen(viewModel: RewardsListViewModel, paddingValues: PaddingValues = PaddingValues()) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        val context = LocalContext.current

        when (val viewState = viewModel.viewState.collectAsState().value) {
            RewardsListViewState.EmptyListError -> {
                DrawInfoMessage(
                    infoImage = painterResource(id = R.drawable.ic_baseline_search_empty),
                    message = stringResource(id = R.string.label_empty_search)
                )
            }

            RewardsListViewState.Error -> {
                DrawInfoMessage(
                    infoImage = painterResource(id = R.drawable.ic_baseline_error),
                    message = stringResource(id = R.string.label_generic_error)
                )
            }

            RewardsListViewState.LoadingRewards -> {
                DrawProgressIndicator()
            }

            is RewardsListViewState.SuccessRewards -> {
                DrawExpandableListView(sections = viewState.rewards ) { reward ->
                    Toast.makeText(context, reward.name, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}