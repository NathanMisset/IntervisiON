/*
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import com.example.intervision.ui.MyApplicationTheme
import com.example.intervision.ui.UiString

/**
 *
 * This activity controls the credit screen
 * It contains a image, text and a button to return to the login screen
 *
 */
class ActivityCredits: ComponentActivity()  {
    /** Initialisation */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            screen()
        }
    }
    /** Methodes */
    private fun toLogin() {
        finish()
    }
    /** Composables */
    @PreviewFontScale @Composable
    fun screen() {
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top

            ) {
                Image(
                    painter = painterResource(id = R.drawable.main_icon),
                    contentDescription = UiString.descriptionIconApp,
                    Modifier
                        .fillMaxWidth()
                )

                Column (
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = UiString.makerContentCredits,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Justify
                    )
                    Text(text = UiString.opdrachtContentCredits,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Justify
                    )
                    Text(text = UiString.ideeContentCredits,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Justify
                    )
                    Text(text = UiString.designContentCredits,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Justify
                    )
                }

                OutlinedButton(
                    onClick = {
                        toLogin()
                    },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp)
                ) {
                    Text(text = UiString.backButtonApp)
                }
            }
        }
    }
}