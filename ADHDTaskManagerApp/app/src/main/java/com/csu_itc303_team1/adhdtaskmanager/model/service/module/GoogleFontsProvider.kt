package com.csu_itc303_team1.adhdtaskmanager.model.service.module

import androidx.compose.ui.text.googlefonts.GoogleFont
import com.csu_itc303_team1.adhdtaskmanager.R

interface GoogleFontsProvider {
    val provider: GoogleFont.Provider
        get() = GoogleFont.Provider(
            providerAuthority = "com.google.android.gms.fonts",
            providerPackage = "com.google.android.gms",
            certificates = R.array.com_google_android_gms_fonts_certs
        )
}