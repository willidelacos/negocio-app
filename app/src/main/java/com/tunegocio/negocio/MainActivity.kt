
package com.tunegocio.negocio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tunegocio.negocio.presentation.navigation.AppNavigation
import com.tunegocio.negocio.ui.theme.NegocioTheme
import com.tunegocio.negocio.data.local.UserSessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userSessionManager = UserSessionManager(this)

        setContent {
            NegocioTheme {
                AppNavigation(userSessionManager)
            }
        }
    }
}
