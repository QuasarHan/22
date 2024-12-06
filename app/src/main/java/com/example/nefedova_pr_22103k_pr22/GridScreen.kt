package com.example.nefedova_pr_22103k_pr22

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GridScreen(cols: Int, rows: Int, picturePrefix: String) {
    val context = LocalContext.current
    // Создаем список ресурсов изображений, перемешанный случайным образом
    val resourceList = remember { makeShuffledPictureArray(context, cols, rows, picturePrefix) }
    // Состояние ячеек (закрытые, открытые или удаленные)
    val cellStates = remember { mutableStateListOf<Status>().apply { repeat(cols * rows) { add(Status.CELL_CLOSE) } } }
    var firstSelectedCell by remember { mutableIntStateOf(-1) }

    // Отображение сетки
    LazyVerticalGrid(
        columns = GridCells.Fixed(cols), // Указываем количество столбцов
        modifier = Modifier.fillMaxSize(), // Занимаем всю доступную область
        contentPadding = PaddingValues(8.dp), // Отступы внутри сетки
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Расстояние между столбцами
        verticalArrangement = Arrangement.spacedBy(8.dp) // Расстояние между строками
    ) {
        items(cellStates.size) { index ->
            GridItem(
                resourceId = resourceList[index],
                status = cellStates[index],
                onClick = {
                    handleCellClick(
                        index = index,
                        resourceList = resourceList,
                        cellStates = cellStates,
                        firstSelectedCell = firstSelectedCell,
                        onFirstCellSelected = { firstSelectedCell = it },
                        onResetSelection = { firstSelectedCell = -1 }
                    )
                }
            )
        }
    }
}

fun makeShuffledPictureArray(context: Context, cols: Int, rows: Int, picturePrefix: String): List<Int> {
    val totalPairs = (cols * rows) / 2
    val resourceList = mutableListOf<Int>()

    for (i in 1..totalPairs) {
        val resourceId = context.resources.getIdentifier("$picturePrefix$i", "drawable", context.packageName)
        if (resourceId == 0) throw IllegalArgumentException("Resource not found for $picturePrefix$i")
        resourceList.add(resourceId)
        resourceList.add(resourceId)
    }

    return resourceList.shuffled()
}

fun handleCellClick(
    index: Int,
    resourceList: List<Int>,
    cellStates: MutableList<Status>,
    firstSelectedCell: Int,
    onFirstCellSelected: (Int) -> Unit,
    onResetSelection: () -> Unit
) {
    if (cellStates[index] != Status.CELL_CLOSE) {
        // Игнорируем клики по открытым или удаленным ячейкам
        return
    }

    if (firstSelectedCell == -1) {
        // Открываем первую выбранную ячейку
        onFirstCellSelected(index)
        openCell(index, cellStates)
    } else {
        // Проверяем вторую ячейку
        openCell(index, cellStates)
        if (checkIfMatch(firstSelectedCell, index, resourceList)) {
            // Если картинки совпали, удаляем их
            deleteCells(firstSelectedCell, index, cellStates)
        } else {
            // Если картинки не совпали, закрываем их через небольшую задержку
            closeCellsWithDelay(firstSelectedCell, index, cellStates)
        }
        onResetSelection()
    }
}

fun closeCellsWithDelay(first: Int, second: Int, cellStates: MutableList<Status>) {
    android.os.Handler().postDelayed({
        closeCells(first, second, cellStates)
    }, 1000) // Задержка 1 секунда
}

fun openCell(position: Int, cellStates: MutableList<Status>) {
    if (cellStates[position] != Status.CELL_DELETE) {
        cellStates[position] = Status.CELL_OPEN
    }
}

fun closeCells(first: Int, second: Int, cellStates: MutableList<Status>) {
    cellStates[first] = Status.CELL_CLOSE
    cellStates[second] = Status.CELL_CLOSE
}
fun deleteCells(first: Int, second: Int, cellStates: MutableList<Status>) {
    cellStates[first] = Status.CELL_DELETE
    cellStates[second] = Status.CELL_DELETE
}

fun checkIfMatch(first: Int, second: Int, resourceList: List<Int>): Boolean {
    return resourceList[first] == resourceList[seco]}