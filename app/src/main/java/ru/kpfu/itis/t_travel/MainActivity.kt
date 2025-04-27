package ru.kpfu.itis.t_travel

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.t_travel.presentation.screens.trips.ui.TripScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            MaterialTheme{
                TripScreen (
                    onNavigateToTripDetail = {tripId->
                        //ToDo
                    }
                )
            }
        }
    }
}