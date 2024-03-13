package com.example.intervision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.intervision.ui.MyApplicationTheme
class ActivityCredits: ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultPreview()
        }
    }
    fun toLogin(){
        finish()
    }
    @Preview(device = "id:Motorola Moto G8 Plus", showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
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
                    painter = painterResource(id = R.drawable.image_main_icon),
                    contentDescription = stringResource(id = R.string.content_1),
                    Modifier
                        .fillMaxWidth()
                )
                Text(text = "In opdracht van Arnt Mein en het Lectoraat Legal Management van de Hogeschool van Amsterdam",
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Justify)
                Text(text = "Idee gebaseerd op DillemApp van [Naam Universiteit] en het Beterbezwaarspel",
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Justify)
                Text(text = "Gebaseerd op het design van Raquel de Romas student Communicatie en Multimedia Design aan de Hogeschool van Amsterdam",
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Justify)
                Text(text = "Gemaakt door Nathan Misset student HBO-ICT Game Development aan de Hogeschool van Amsterdam",
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Justify)
                OutlinedButton(
                    onClick = {
                        toLogin()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp)
                )
                {
                    Text(text = "Terug")
                }
            }
        }
    }
}