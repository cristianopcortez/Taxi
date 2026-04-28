package br.com.ccortez.core.common.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDialog(
    dialogTitle: String, // Add dialogTitle parameter
    dialogText: String,  // Add dialogText parameter
    onConfirmAction: () -> Unit
) {


    val openAlertDialog = remember { mutableStateOf(true) }

    when {
        openAlertDialog.value -> {
            ConfirmDialog2(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = onConfirmAction, // Pass the function here
                dialogTitle = dialogTitle,
                dialogText = dialogText,
                icon = Icons.Default.Info
            )
        }
    }
}

@Composable
fun ConfirmDialog2(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit, // This parameter accepts a function
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation() // Call the passed function here
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun LoadingIndicatorWithText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally in the Column
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp), // Use .size instead of .width and .height for square
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp)) // Add some space between indicator and text (optional)
        Text("Loading...")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.setTagAndId(tag: String): Modifier {
    return this
        .semantics { testTagsAsResourceId = true }
        .testTag(tag)
}