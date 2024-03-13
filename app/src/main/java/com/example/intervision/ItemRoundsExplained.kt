package com.example.intervision

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.spacing

class ItemRoundsExplained  {
    @Preview(device = "spec:width=1080px,height=2280px,dpi=400")
    @Composable
    fun Component() {

            Column {
                Text(
                    text = "Ronde 1 van 5",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text = "Introductie",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = spacing.large)
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "De stellingen ",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
                Text(
                    text =
                    "\n 1. De stelling introduceren" +
                            "\n 2. Toelichting keuzes van de deelnermers" +
                            "\n 3. Discussie met elkaar over stelling" +
                            "\n 4. Wat gaan de deelnemers anders doen?" +
                            "\n 5. Mogelijkheid voor het behandelen van nieuwe stelling"
                )
            }
    }
}