package com.example.homework_1_compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homework_1_compose.ui.theme.Homework1composeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Homework1composeTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val listSaver = listSaver<SnapshotStateList<Int>, Int>(
        save = { stateList ->
            if (stateList.isNotEmpty()) {
                val first = stateList.first()
                if (!canBeSaved(first)) {
                    throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                }
            }
            stateList.toList()
        },
        restore = { it.toMutableStateList() }
    )

    val digits = rememberSaveable(stateSaver = listSaver) { mutableStateOf(mutableStateListOf(0)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(0.9f)) {
            DigitsList(digits = digits.value)
        }

        OutlinedButton(
            onClick = {
                digits.value.add(digits.value.size)
            },
            modifier = Modifier
                .weight(0.1f),
            shape = CircleShape,
            border = BorderStroke(5.dp, Color.Black),
            contentPadding = PaddingValues(0.dp),
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "content description",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun DigitsList(digits: List<Int>) {
    val state = LazyGridState()
    val coroutineScope = rememberCoroutineScope()
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) GridCells.Fixed(
            4
        ) else GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        state = state
    ) {
        items(digits) { item ->
            Card(
                modifier = Modifier
                    .padding(10.dp),
                shape = RectangleShape,
                backgroundColor = if (item % 2 == 1) Color.Blue else Color.Red,
            ) {
                Text(
                    text = item.toString(),
                    fontSize = 42.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }

        }
        coroutineScope.launch {
            state.animateScrollToItem(digits.size - 1)
        }
    }
}


