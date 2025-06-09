package ru.kpfu.itis.t_travel.presentation.common

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AvatarManager @Inject constructor(
    private val context: Context
) {
    private val avatarsDir: File by lazy {
        File(context.filesDir, "avatars").apply { mkdirs() }
    }

    fun saveAvatar(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "avatar_${System.currentTimeMillis()}.jpg"
        val file = File(avatarsDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }

    fun getAvatarUri(path: String?): Uri? {
        return path?.let { Uri.fromFile(File(it)) }
    }

    fun clearAvatar(path: String?) {
        path?.let { File(it).delete() }
    }
}