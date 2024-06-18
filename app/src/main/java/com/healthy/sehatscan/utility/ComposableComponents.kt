package com.healthy.sehatscan.utility

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.healthy.sehatscan.R
import com.healthy.sehatscan.ui.theme.onPrimaryContainerLight
import com.healthy.sehatscan.ui.theme.primaryContainerLight

@Composable
fun LoadingDialog(text: String = stringResource(R.string.loading)) {
    Dialog(onDismissRequest = { /*NO-OP*/ }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                strokeWidth = 4.dp,
                color = onPrimaryContainerLight,
                trackColor = primaryContainerLight,
                modifier = Modifier
                    .size(64.dp)
            )
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = primaryContainerLight
            )
        }
    }
}