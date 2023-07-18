package com.example.gameapplication.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import com.example.gameapplication.R

data class FarmCardModel(
    @DrawableRes
    var icon: Int = R.drawable.icon_garlic,
    var isSelected: Boolean = false,
    var isRight: Boolean = false,
    var isClickable: Boolean = false,
    var isVisible: MutableState<Boolean>
)
