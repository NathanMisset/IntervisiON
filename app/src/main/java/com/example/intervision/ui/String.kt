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
    val taalCodeApp: String = "nl",
    val deviceIdApp: String = "id:Motorola Moto G8 Plus",
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

    /** Register */
    val voornaamErrorRegister: String = "Voornaam is verplich",
    val werkfuntieErrorRegister: String = "Werkfunctie is verplicht",
    val emailErrorRegister: String = "Email is verplicht",
    val emailFormatErrorRegister: String = "Email format niet juist",
    val wachtwoordErrorRegister: String = "Wachtwoord is verplicht",
    val wachtwoordLengtenErrorRegister: String = "Wachtwoord is verplicht",
    val voornaamLabelRegister: String = "*Voornaam",
    val werkfuntieLabelRegister: String = "*Werkfunctie",
    val emailLabelRegister: String = "*Email",
    val wachtwoordLabelRegister: String = "*Wachtwoord",
    val wachtwoordZichbaarLabelRegister: String = "Show password",
    val wachtwoordOntzichbaarRegister: String = "Hide password",
    val registerRegister: String = "Register",
    val toestemmingRegister: String = "Ik verleen toestemming voor het anoniem bewaren van mijn stemmen op de stellingen ten behoeve van onderzoeksdoeleinden " +
            "van het lectoraat Legal Management",
    val finishRegister: String = "Register",

    /** Reset password */
    val verzendenLabelResetPassword: String = "Verzenden",
    val contentResetPassword: String = "Vul je email in.\\n\\nAls de email gelinked is \" +\n" +
            "                                \"aan een account\\nkrijgt u een mail met een link om \\n\" +\n" +
            "                                \"wachtwoord te reseten.",


    /** Credits */
    val makerContentCredits: String = "Gemaakt door Nathan Misset student HBO-ICT Game Development aan de Hogeschool van Amsterdam in 2024",
    val opdrachtContentCredits: String = "In opdracht van het Lectoraat Legal Management van de Hogeschool van Amsterdam",
    val ideeContentCredits: String = "Idee gebaseerd op Dielemma game van de Irasmus Universiteit en het Beterbezwaarspel van het lectoraat legal Managment",
    val designContentCredits: String = "Gebaseerd op het design van Raquel de Romas student Communicatie en Multimedia Design aan de Hogeschool van Amsterdam",

    /** Intervisie */
    val wachtTekstIntervisie: String = "Wacht tot de leider een beslissing maakt",

    /** Tutorial */
    val primeHeaderTutorial: String = "HOE WERKT DE APP?",
    val header1Tutorial: String = "1. Elke maand\\n een nieuwe stelling",
    val header2Tutorial: String = "2. Eens of Oneens?",
    val header3Tutorial: String = "3. Online intervisie",
    val content1Tutorial: String = "Elke eerste maandag van de maand om 16:00 ontvang je een stelling van de app met betrekking tot een bezwaarprocedure.",
    val content2Tutorial: String = "Na het openen van de stelling geef je aan of je het eens of oneens ben met de stelling.\\n \\n Optioneel: Iedereen mag nieuwe stellingen in de app toevoegen",
    val content3Tutorial: String = "Na het stemmen heb je de optie om een online intervisie te houden over de desbetreffende stelling samen met jouw collega’s of andere juridische experts binnen of buiten jouw regio via Microsoft Teams.\\n \\nWij zorgen ervoor dat jij direct een Teams-uitnodiging ontvangt na het deelnemen aan een intervisie.",
    val photo1DescriptionTutorial: String = "Voorbeeld van notificatie",
    val photo2DescriptionTutorial: String = "Voorbeeld van een stelling in de app",
    val photo3DescriptionTutorial: String = "foto van een teams gesprek op een scherm plus mobiel met de intervisie open",

    /** Waiting Room */
    val desciptionWaitingRoom: String = "Open nu de Teams vergadering op jouw desktop/laptop voordat de intervisie begint en zorg ervoor dat de verbinding technisch werkt!",
    val startButtonWaitingRoom: String = "Start",
    val leaveButtonWaitingRoom: String = "Verlaten",
    val intentPutExtraSessionIDWaitingRoom: String = "SessionID",
    val intentPutExtraLeaderWaitingRoom: String = "Leader",
    val profile1ImageDescriptionWaitingRoom: String = "ProfilePicture 1",
    val profile2ImageDescriptionWaitingRoom: String = "ProfilePicture 2",
    val profile3ImageDescriptionWaitingRoom: String = "ProfilePicture 3",
    val profile4ImageDescriptionWaitingRoom: String = "ProfilePicture 4",
    val profile5ImageDescriptionWaitingRoom: String = "ProfilePicture 5",
    val profile6ImageDescriptionWaitingRoom: String = "ProfilePicture 6",

    /** FragmentGroup */
    val maakGroupFragmentGroup: String = "Maak groep",

    /** FragmentProfile */
    val settingsDescriptionProfileGroup: String = "settings",

    /** Settings */
    val acountLabelSettings: String = "Acount",
    val opslaanLabelSettings: String = "Opslaan",
    val quitIconDescriptonSettings: String = "Close Cross",

    )

val LocalString = compositionLocalOf { com.example.intervision.ui.String() }
val LocalString1 =  com.example.intervision.ui.String()

val UiString: com.example.intervision.ui.String
    get() = LocalString1
val ComposableUiString: com.example.intervision.ui.String
    @Composable
    @ReadOnlyComposable
    get() = LocalString.current