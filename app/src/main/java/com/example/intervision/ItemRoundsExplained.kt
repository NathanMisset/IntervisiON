/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.ComposableUiString
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.spacing

/**
 *
 * This item can be initiated as an object in an activity
 * This item show the introduction round
 *
 */

class ItemRoundsExplained  {
    @Composable
    fun Screen() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ComposableUiString.titleItemRoundsExplained,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
            }
            Text(
                text = ComposableUiString.headerItemRoundsExplained,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(horizontal = spacing.large)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = ComposableUiString.contentItemRoundsExplained
                )
            }
        }
    }

    @Composable
    fun ScreenUser() {
        IntervisionBaseTheme {
            Column {
                Text(
                    text = ComposableUiString.titleItemRoundsExplained,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text = ComposableUiString.underTitleItemRoundsExplained,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )
            }
            Text(
                text = ComposableUiString.headerItemRoundsExplained,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = spacing.large)
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = ComposableUiString.contentItemRoundsExplained
                )
            }
        }
    }
}
@PreviewFontScale @Composable
fun ItemRoundsExplainedPreview() {
    IntervisionBaseTheme {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ComposableUiString.titleItemRoundsExplained,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
            }
            Text(
                text = ComposableUiString.headerItemRoundsExplained,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(horizontal = spacing.large)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = ComposableUiString.contentItemRoundsExplained
                )
            }
        }
    }
}