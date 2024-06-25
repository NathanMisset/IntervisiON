/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

import kotlin.String

data class String(
    /** App */
    val nameApp: String = "Intervisie voor bezwaarbehandelaars",
    val quitButtonApp: String = "Sluit",
    val saveButtonApp: String = "Opgeslagen",
    val backButtonApp: String = "Terug",
    val nextButtonApp: String = "Volgende",
    val descriptionIconApp: String = "App Icon",

    /** App */
    val loginButtonLogin: String = "Login",
    val registerButtonLogin: String = "Registeren",
    val forgotPasswordButtonLogin: String = "Wachtwoord vergeten?",
    val creditsButtonLogin: String = "Credits",
    val emailTextFieldLogin: String = "Email",
    val passwordTextFieldLogin: String = "Wachtwoord",

    /** Navitation */
    val homeLabelNavigation: String = "Home",
    val groupLabelNavigation: String = "Group",
    val profileLabelNavigation: String = "Profile",

    /** Credits */
    val makerContentCredits: String = "Gemaakt door Nathan Misset student HBO-ICT Game Development aan de Hogeschool van Amsterdam in 2024",
    val opdrachtContentCredits: String = "In opdracht van het Lectoraat Legal Management van de Hogeschool van Amsterdam",
    val ideeContentCredits: String = "Idee gebaseerd op Dielemma game van de Irasmus Universiteit en het Beterbezwaarspel van het lectoraat legal Managment",
    val designContentCredits: String = "Gebaseerd op het design van Raquel de Romas student Communicatie en Multimedia Design aan de Hogeschool van Amsterdam",

    /** Intervisie */
    val wachtTekstIntervisie: String = "Wacht tot de leider een beslissing maakt"
    )

val LocalString= compositionLocalOf { com.example.intervision.ui.String() }

val UiString: com.example.intervision.ui.String
    @Composable
    @ReadOnlyComposable
    get() = LocalString.current