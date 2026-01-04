package com.example.design_system.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.design_system.R
import com.example.design_system.theme.mTypography


private val ErrorLabel = R.string.error_label

@Composable
fun ErrorSection() {
    // Load Lottie composition once and remember it across recompositions
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.error_animation)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error animation (infinite loop)
            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(170.dp),
                iterations = LottieConstants.IterateForever
            )

            // Error label text
            Text(
                text = stringResource(ErrorLabel),
                style = mTypography.bodyLarge.copy(lineBreak = LineBreak.Paragraph),
                textAlign = TextAlign.Center
            )
        }
    }
}