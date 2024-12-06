package com.example.nefedova_pr_22103k_pr22

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.nefedova_pr_22103k_pr22.ui.theme.Nefedova_pr_22103k_pr22Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nefedova_pr_22103k_pr22Theme {
                GridScreen(cols = 4, rows = 8, picturePrefix = "kart")
                }
            }
        }
    }


