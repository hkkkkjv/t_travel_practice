package ru.kpfu.itis.t_travel.presentation.screens.more.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.presentation.common.AppLanguage
import ru.kpfu.itis.t_travel.presentation.common.AppTheme
import ru.kpfu.itis.t_travel.presentation.common.ui.ErrorDialog
import ru.kpfu.itis.t_travel.presentation.common.ui.SecondaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBarWithBack

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    InternalProfileScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun InternalProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit
) {
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onEvent(ProfileEvent.AvatarPicked(it)) }
    }
    Scaffold(topBar = {
        TransparentTopAppBarWithBack(
            title = stringResource(R.string.profile),
            onBackClick = { onEvent(ProfileEvent.BackClicked) }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.user != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        modifier = Modifier
                            .size(96.dp),
                        shape = CircleShape,
                        onClick = { pickImageLauncher.launch("image/*") },
                        elevation = CardDefaults.cardElevation(8.dp),
                    ) {
                        if (state.avatarUri != null) {
                            AsyncImage(
                                model = state.avatarUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_profile_more),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = state.user.firstName + " " + state.user.lastName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = state.user.phone,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = { onEvent(ProfileEvent.EditProfileClicked) },
                        Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.update_profile),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(Modifier.height(36.dp))
            }
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onEvent(ProfileEvent.ThemeClicked) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.theme), Modifier.weight(1f))
                        Text(state.theme.displayName())
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                    Spacer(Modifier.height(24.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onEvent(ProfileEvent.LanguageClicked) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.language), Modifier.weight(1f))
                        Text(state.language.displayName)
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            SecondaryButton(
                text = stringResource(R.string.logout),
                onClick = { onEvent(ProfileEvent.LogoutClicked) },
            )
            if (state.error != null) {
                ErrorDialog(
                    onDismiss = { onEvent(ProfileEvent.ErrorDismissed) },
                    error = state.error,
                )
            }
        }
    }
    if (state.showThemeDialog) {
        ThemeDialog(
            selectedTheme = state.theme,
            onThemeSelected = { onEvent(ProfileEvent.ThemeSelected(it)) },
            onDismiss = { onEvent(ProfileEvent.ThemeDialogDismissed) }
        )
    }

    if (state.showLanguageDialog) {
        LanguageDialog(
            selectedLanguage = state.language,
            onLanguageSelected = { onEvent(ProfileEvent.LanguageSelected(it)) },
            onDismiss = { onEvent(ProfileEvent.LanguageDialogDismissed) }
        )
    }
}

@Composable
fun ThemeDialog(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.choose_theme)) },
        text = {
            Column {
                AppTheme.entries.forEach { theme ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onThemeSelected(theme) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = theme == selectedTheme,
                            onClick = { onThemeSelected(theme) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(theme.displayName())
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss, colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.secondary
                )
            ) { Text(stringResource(R.string.dismiss)) }
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}

@Composable
fun LanguageDialog(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.choose_language)) },
        text = {
            Column {
                AppLanguage.entries.forEach { lang ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(lang) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = lang == selectedLanguage,
                            onClick = { onLanguageSelected(lang) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(lang.displayName)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss, colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.secondary
                )
            ) { Text(stringResource(R.string.dismiss)) }
        },
        containerColor = MaterialTheme.colorScheme.background

    )
}