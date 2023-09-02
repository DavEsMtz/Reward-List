package com.example.rewardlist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.example.rewardlist.ui.composable.DrawTopBar
import com.example.rewardlist.ui.list.ListScreen
import com.example.rewardlist.ui.list.RewardsListViewModel
import com.example.rewardlist.ui.theme.RewardListTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: RewardsListViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.populateRewards()
        setContent {
            RewardListTheme {
                // A surface container using the 'background' color from the theme

                Scaffold(
                    topBar = { DrawTopBar(refresh = viewModel::populateRewards) }
                ) { paddingValues ->
                    ListScreen(viewModel, paddingValues)
                }
            }
        }
    }
}