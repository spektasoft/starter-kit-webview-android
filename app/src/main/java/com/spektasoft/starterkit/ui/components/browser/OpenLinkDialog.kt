package com.spektasoft.starterkit.ui.components.browser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spektasoft.starterkit.R

@Composable
fun OpenLinkDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    link: String
) {
    AlertDialog(
        onDismissRequest = onCancel,
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(stringResource(R.string.open_link_confirmation))
                Text(truncateText(link), fontWeight = FontWeight.Light)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.no))
            }
        }
    )
}

private fun truncateText(text: String): String {
    if (text.length <= 100) {
        return text
    } else {
        val first40 = text.take(40)
        val last40 = text.takeLast(40)
        return "$first40...$last40"
    }
}