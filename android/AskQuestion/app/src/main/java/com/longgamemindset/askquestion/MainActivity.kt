package com.longgamemindset.askquestion

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.longgamemindset.askquestion.ui.theme.AskQuestionTheme
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private val store by viewModels<Store>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            AskQuestionTheme {
                val navController = rememberNavController()
                val myValue: StringHolder by store.myValue.observeAsState(StringHolder(""))
                NavHost(navController = navController, startDestination = Screen.TOC.name) {
                    composable(Screen.TOC.name) { Toc(navController) }
                    composable(Screen.NESTED_MODAL_SHEET.name) { NestedModalSheets() }
                    composable(Screen.STORE_UPDATE_LIVE_DATA_OBJECT.name) {
                        StoreUpdateLiveDataObject(myValue, updateValue = store::updateValue)
                    }
                    composable(Screen.STICKY_BOTTOM_BAR.name) {
                        StickyBottomBar()
                    }
                    composable(Screen.RESET_AND_CLEAR.name) {
                        ResetAndClearTextField()
                    }
                }
            }
        }
    }
}

enum class Screen {
    TOC,
    NESTED_MODAL_SHEET,
    STORE_UPDATE_LIVE_DATA_OBJECT,
    STICKY_BOTTOM_BAR,
    RESET_AND_CLEAR,
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

@ExperimentalMaterialApi
@Composable
fun StoreUpdateLiveDataObject(myValue: StringHolder, updateValue: (StringHolder) -> Unit) {
    Column {
        Text(makeString())
        Text(getTextHeightForStyle(LocalTextStyle.current, 1).toString())
        Text(getTextHeightForStyle(LocalTextStyle.current, 5).toString())
        Text(getTextHeightForStyle(LocalTextStyle.current, 50).toString())
        Text(getTextHeightForStyle(LocalTextStyle.current, 100).toString())
        OutlinedTextField(
            value = myValue.s,
            onValueChange = {
                myValue.s = it
                val sh = StringHolder(it)
                updateValue(sh)
            },
            label = { Text("Description") }
        )
    }
}

class Store: ViewModel() {
    var _myValue = MutableLiveData(StringHolder(""))
    var myValue: LiveData<StringHolder> = _myValue

    fun updateValue(newValue: StringHolder) {
        _myValue.postValue(newValue)
    }
}

class StringHolder(var s: String) {}

@Composable
fun getTextHeightForStyle(style: TextStyle, lines: Int): Dp {
    return with(LocalDensity.current) {
        Paragraph(
            text = "t".repeat(lines),
            style = style,
            // the Paragraph returned height using maxLines as the lines
            maxLines = lines,
            density = LocalDensity.current,
            resourceLoader = LocalFontLoader.current,
            width = 1f
        )
            .height
            .toDp()
    }
}

fun makeString(): String {
    val sh = TiTask()
    val sh2 = sh.newInstance()
    return "$sh $sh2"
}

class TiTask {
    val id: String
    var modifiedTimestamp: Instant
    var eventTimestamp: Instant
        set(value) {
            field = value
            updateModifiedTimestamp()
        }
    var description: String = ""
        set(value) {
            field = value
            updateModifiedTimestamp()
        }
    var minutesUsed: Int? = null
        set(value) {
            field = value
            updateModifiedTimestamp()
        }
    var endTimestamp: Instant? = null
        set(value) {
            field = value
            updateModifiedTimestamp()
        }

    var tags: Set<String> = setOf(
        "default-tag",
        "default-tag1",
        "default-tag2",
        "default-tag3",
        "default-tag4",
        "default-tag5"
    )
        set(value) {
            field = value
            updateModifiedTimestamp()
        }

    constructor(
        id: String = "${Instant.now().epochSecond}-${UUID.randomUUID()}",
        eventTimestamp: Instant = Instant.now()
    ) {
        this.id = id
        this.eventTimestamp = eventTimestamp
        this.modifiedTimestamp = Instant.now()
    }

    fun newInstance(): TiTask {
        val newTask = TiTask(id = id, eventTimestamp = eventTimestamp)
        newTask.description = description
        newTask.minutesUsed = minutesUsed
        newTask.endTimestamp = endTimestamp
        newTask.tags = tags
        // This has to be set the end because assignments updates the newTask.modifiedTimestamp
        newTask.modifiedTimestamp = modifiedTimestamp
        return newTask
    }

    private fun updateModifiedTimestamp() {
        modifiedTimestamp = Instant.now()
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + modifiedTimestamp.hashCode()
        result = 31 * result + eventTimestamp.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (minutesUsed ?: 0)
        result = 31 * result + (endTimestamp?.hashCode() ?: 0)
        result = 31 * result + tags.hashCode()
        return result
    }
}

@Composable
fun StickyBottomBar() {
    Scaffold(bottomBar = { MyBottomBar() }) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            repeat(3) {
                Text("Some text $it")
            }
            var t by remember { mutableStateOf("") }
            OutlinedTextField(
                value = t,
                onValueChange = {
                    t = it
                },
                label = { Text("Description") }
            )
            repeat(20) {
                Text("bottom has some text $it")
            }
        }
    }
}

@Composable
private fun MyBottomBar() {
    var expanded by remember { mutableStateOf(false) }
    BottomAppBar {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Box {
                IconButton(
                    onClick = {expanded = true}
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "more"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        Log.i("mytag", "this is not reached")
                        expanded = false
                    }
                ) {
                    DropdownMenuItem(onClick = { expanded = false }) {
                        Text("item1")
                    }
                    DropdownMenuItem(onClick = { expanded = false }) {
                        Text("item2")
                    }
                }
            }
        }
    }
}

@Composable
private fun ResetAndClearTextField() {
    val (textFieldValue, setTextFieldValue) = remember { mutableStateOf(
        TextFieldValue("")
    ) }
    val focusManager = LocalFocusManager.current
    Column {
        TextField(
            value = textFieldValue,
            onValueChange = setTextFieldValue
        )

        Button(onClick = {
            setTextFieldValue(TextFieldValue(""))
            focusManager.clearFocus()
        }) {
            Text("reset and clear")
        }
    }
}