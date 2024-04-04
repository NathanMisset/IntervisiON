package com.example.intervision

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervision.ui.MyApplicationTheme
import com.example.intervision.ui.spacing

class ItemRoundsExplained  {
    @Composable
    fun Component() {
        MyApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Introductie ronde",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
            }
            Text(
                text = "Uitleg",
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
                    text =
                    "\n 1. In de eerste ronden introduceert U (de groupsleider) de stelling en ziet iedereen het resultaat van het stemmen" +
                            "\n 2. In de tweede ronden geef je spelers de mogelijkheid hun keuze te onderbouwen" +
                            "\n 3. In de derde ronde is het ruimte voor discussie" +
                            "\n 4. In de vierde ronde vraagt u de spelers wat ze anders gaan doen" +
                            "\n 5. In de vijfde ronden wordt het spel beeindigt"
                )
            }
        }
    }

    @Composable
    fun ComponentParticipant() {
        MyApplicationTheme {
            Column {
                Text(
                    text = "Introductie ronde",
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
            Text(
                text = "Uitleg",
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
                    text =
                    "\n 1. In de eerste ronden introduceert de groupsleider de stelling en kunt u het resultaat van het stemmen zien" +
                            "\n 2. In de tweede ronden krijgt u en de ander spelers de beurt om uw keuze te onderbouwen" +
                            "\n 3. In de derde ronde is het ruimte voor discussie" +
                            "\n 4. In de vierde ronde wordt gevraagt wat u van af nu ander gaat proberen te doen" +
                            "\n 5. In de vijfde ronden wordt het spel beeindigt"
                )
            }
        }
    }
}
@PreviewFontScale
@Composable
fun TestComponent() {
    MyApplicationTheme {
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
                    text = "Introductie ronde",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )
            }
            Text(
                text = "Uitleg",
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
                    text =
                    "\n 1. In de eerste ronden introduceert U (de groupsleider) de stelling en ziet iedereen het resultaat van het stemmen" +
                            "\n\n 2. In de tweede ronden geef je spelers de mogelijkheid hun keuze te onderbouwen" +
                            "\n\n 3. In de derde ronde is het ruimte voor discussie" +
                            "\n\n 4. In de vierde ronde vraagt u de spelers wat ze anders gaan doen" +
                            "\n\n 5. In de vijfde ronden wordt het spel beeindigt"
                )
            }
        }
    }
}