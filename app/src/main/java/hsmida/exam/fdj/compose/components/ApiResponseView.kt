package hsmida.exam.fdj.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hsmida.exam.fdj.R

@Composable
fun ApiResponseView(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isEmpty: Boolean,
    emptyMessage: String,
    isError: Boolean,
    errorMessage: String,
    onRetryClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    when {
        isLoading -> Box(modifier = modifier) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        isEmpty -> Box(modifier = modifier) {
            Text(
                modifier = Modifier.align(Alignment.Center), text = emptyMessage
            )
        }

        isError -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = errorMessage,
                textAlign = TextAlign.Center
            )
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = { onRetryClicked() }) {
                Text(text = stringResource(id = R.string.retry))
            }
        }

        else -> content()
    }
}