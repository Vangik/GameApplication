package com.example.gameapplication.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameapplication.R
import com.example.gameapplication.model.FarmCardModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FarmGameViewModel : ViewModel() {
    private var tries: Int = 0
    private var vegetablesNumber = 0
    var fieldWidth = 4
    var fieldHeight = 4
    var score = mutableStateOf(0)
    var resultVisibility = mutableStateOf(false)
    var successResult = mutableStateOf(false)
    var cards = mutableStateOf<List<FarmCardModel>>(listOf())

    private val vegetablesImages = listOf(
        R.drawable.icon_cabbage,
        R.drawable.icon_leek,
        R.drawable.icon_beet,
        R.drawable.icon_corn,
        R.drawable.icon_cucumber,
        R.drawable.icon_peas
    )

    fun loadGameField(level: Int) {
        resultVisibility.value = false
        successResult.value = false
        score.value = 0
        vegetablesNumber = level + 3
        tries = vegetablesNumber
        when {
            level < 4 -> {
                fieldWidth = 4
                fieldHeight = vegetablesNumber
            }
            else -> {
                fieldWidth = 5
                fieldHeight = vegetablesNumber - 1
            }
        }
        val cardsList = mutableListOf<FarmCardModel>()
        viewModelScope.launch {
            cardsList.addAll(generateCardsList())
        }
        cards.value = cardsList
    }

    private suspend fun generateCardsList(): List<FarmCardModel> {
        val cardsList = mutableListOf<FarmCardModel>()
        val addWrongCards = viewModelScope.async {
            repeat(fieldWidth * fieldHeight - vegetablesNumber) {
                cardsList.add(
                    FarmCardModel(
                        isVisible = mutableStateOf(false)
                    )
                )
            }
        }
        val addCorrectCards = viewModelScope.async {
            repeat(vegetablesNumber) {
                cardsList.add(
                    FarmCardModel(
                        icon = vegetablesImages[
                                if (it < vegetablesImages.size) it
                                else it - vegetablesImages.size
                        ],
                        isRight = true,
                        isVisible = mutableStateOf(true)
                    )
                )
            }
            cardsList.shuffle()
        }
        addWrongCards.await()
        addCorrectCards.await()
        return cardsList
    }

    fun onClick(value: Boolean) {
        if (value) {
            score.value += 5
        }
        tries--
        if (tries == 0) {
            resultVisibility.value = true
            cards.value.map {
                if (it.isRight)
                    it.isVisible.value = true
                it.isClickable = false
            }
            if ((score.value / 5) == vegetablesNumber)
                successResult.value = true
        }
    }
}