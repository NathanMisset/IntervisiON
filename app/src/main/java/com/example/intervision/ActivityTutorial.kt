/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.intervision.ui.IntervisionBaseTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

/**
 *
 * This activity uses a viewpager to display the tutorial
 * afterwards user will be sent to
 *
 */

class ActivityTutorial : ComponentActivity() {

    /** Initialisation */
    data class OnboardingPage(
        val background: Color,
        var title: String,
        val stepNumber: Int,
        @DrawableRes val image: Int
    )

    private val onboardPages = listOf(
        OnboardingPage(
            background = Color(0xFFFFC40A),
            title = "",
            stepNumber = 1,
            image = R.drawable.image4
        ),
        OnboardingPage(
            background = Color(0xFF004B51),
            title = "",
            stepNumber = 2,
            image = R.drawable.image5
        ),
        OnboardingPage(
            background = Color(0xFF2E492E),
            title = "",
            stepNumber = 3,
            image = R.drawable.image6
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                OnboardingUI()
            }
        }
        prepStrings()
    }

    private fun prepStrings(){
        onboardPages[0].title = getString(R.string.content1Tutorial)
        onboardPages[1].title = getString(R.string.content2Tutorial)
        onboardPages[2].title = getString(R.string.content3Tutorial)
    }

    private fun toNavigation() {
        val i = Intent(this, ActivityNavigation::class.java)
        startActivity(i)
    }

    @Preview @OptIn(ExperimentalPagerApi::class, ExperimentalPagerApi::class) @Composable
    fun OnboardingUI() {
        IntervisionBaseTheme {
            val pagerState = rememberPagerState(pageCount = 3)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
            ) {
                HorizontalPager(
                    state = pagerState, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    OnboardingScreen(page = onboardPages[page])
                }
                HorizontalPagerIndicator(
                    pagerState = pagerState, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    activeColor = Color.Black
                )
                AnimatedVisibility(visible = pagerState.currentPage == 2) {
                    OutlinedButton(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(start = 24.dp, top = 14.dp, end = 24.dp, bottom = 8.dp),
                        onClick = { toNavigation() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.startButtonTutorial),
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun OnboardingScreen(page: OnboardingPage) {
        IntervisionBaseTheme {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = page.image), contentDescription = stringResource(R.string.stepNDescriotionTutorial),
                    modifier = Modifier.size(300.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.width(70.dp).height(18.dp)
                        .clip(CircleShape)
                        .background(color = page.background),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.stepDescriptionTutorial) + page.stepNumber.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    text = page.title,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    companion object {
        private const val TAG = "HandleidingActivity"
    }
}