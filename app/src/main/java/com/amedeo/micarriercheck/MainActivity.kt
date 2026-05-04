package com.amedeo.micarriercheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val result = detectCarrier()
        setContent {
            CarrierApp(result)
        }
    }
}
