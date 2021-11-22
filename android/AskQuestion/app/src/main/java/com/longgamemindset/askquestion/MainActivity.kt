package com.longgamemindset.askquestion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.longgamemindset.askquestion.ui.theme.AskQuestionTheme
import kotlinx.coroutines.launch
import java.util.*

class StrHolder(var s: String)

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AskQuestionTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.TOC.name) {
                    composable(Screen.TOC.name) { Toc(navController) }
                    composable(Screen.NESTED_MODAL_SHEET.name) { NestedModalSheets() }
                }
            }
        }
    }
}

enum class Screen {
    TOC,
    NESTED_MODAL_SHEET
}

@ExperimentalMaterialApi
@Composable
fun Toc(navHostController: NavHostController) {
    LazyColumn {
        items(Screen.values()) {
            ListItem(modifier = Modifier.clickable { navHostController.navigate(it.name) }) {
                Text(it.name)
            }

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun NestedModalSheets() {
    val firstModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = firstModalBottomSheetState,
        sheetContent = {
            FirstModalSheet()
        }
    ) {
        Column {
            Text("Root")
            Button(onClick = { scope.launch {firstModalBottomSheetState.show()} }) {
                Text("Go to the first sheet")
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun FirstModalSheet() {
    val secondModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = secondModalBottomSheetState,
        sheetContent = {
            Column(modifier = Modifier.zIndex(1.0f)) {
                Text("Second")
                Text("Second")
                Text("Second")
                Text("Second")
                Text("Second")
                Text("Second")
                Text("Second")
                Text("Second")
            }
        }
    ) {
        Column {
            Text("First")
            Button(onClick = { scope.launch { secondModalBottomSheetState.show() } }) {
                Text("Go to the second sheet")
            }
        }
    }
}