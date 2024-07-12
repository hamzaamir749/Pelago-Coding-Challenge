package com.pelagohealth.codingchallenge.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pelagohealth.codingchallenge.presentation.MainViewModel
import com.pelagohealth.codingchallenge.presentation.components.SwipeableCard

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart).verticalScroll(rememberScrollState())
        ) {
            viewModel.state.fact?.let { facts ->
                for (fact in facts) {
                    SwipeableCard(fact = fact, position = facts.indexOf(fact)) { position ->
                        viewModel.deleteItem(position)
                    }
                }
            }
        }

        viewModel.state.error?.let { error ->
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        }

        Button(
            onClick = { viewModel.fetchNewFact() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            when (viewModel.state.isLoading) {
                true -> {
                    Text("Loading...")
                }
                false -> {
                    Text("More facts!")
                }
            }
        }
    }
}
