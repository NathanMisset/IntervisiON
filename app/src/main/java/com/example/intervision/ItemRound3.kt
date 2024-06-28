/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.ComposableUiString
import com.example.intervision.ui.IntervisionBaseTheme
import com.example.intervision.ui.spacing

/**
 *
 * This item can be initiated as an object in an activity
 * This item is the 3th round and consiste of a title and an image
 *
 */

class ItemRound3 {
    @Composable
    fun Screen() {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = ComposableUiString.roundTextItemRound3,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp
            )
            Text(
                text = ComposableUiString.asignmentItemRound3,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(horizontal = spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(0.9f),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                painter = painterResource(id = R.drawable.image_discussing_192x192),
                contentDescription = ComposableUiString.buttonTextItemFinalRound
            )
        }
    }
    @Composable
    fun ScreenUser() {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = ComposableUiString.roundTextItemRound3,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp
            )
            Text(
                text = ComposableUiString.asignmentItemRound3,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(horizontal = spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(0.9f),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                painter = painterResource(id = R.drawable.image_discussing_192x192),
                contentDescription =  ComposableUiString.buttonTextItemFinalRound
            )
        }
    }
}

@Preview(device = "spec:width=1080px,height=2280px,dpi=400")
@Composable
fun ItemRound3Preview() {
    IntervisionBaseTheme {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = ComposableUiString.roundTextItemRound3,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text = ComposableUiString.asignmentItemRound3,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(horizontal = spacing.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(0.9f),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    painter = painterResource(id = R.drawable.image_discussing_192x192),
                    contentDescription = ComposableUiString.buttonTextItemFinalRound
                )
            }
        }
    }
}


