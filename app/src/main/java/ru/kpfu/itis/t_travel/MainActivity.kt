package ru.kpfu.itis.t_travel

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.t_travel.presentation.screens.trips.ui.TripScreen
import ru.kpfu.itis.t_travel.presentation.theme.TravelTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            TravelTheme{
                TripScreen (
                    onNavigateToTripDetail = {tripId->
                        //TODO
                    }
                )
            }
        }
    }
}