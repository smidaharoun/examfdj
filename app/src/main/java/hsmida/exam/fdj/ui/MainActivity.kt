package hsmida.exam.fdj.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import dagger.hilt.android.AndroidEntryPoint
import hsmida.exam.fdj.R
import hsmida.exam.fdj.compose.ExamApp
import hsmida.exam.fdj.ui.theme.ExamTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamTheme {
                ExamApp()
            }
        }
    }
}