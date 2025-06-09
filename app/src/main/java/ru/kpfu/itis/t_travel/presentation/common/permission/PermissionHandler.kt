package ru.kpfu.itis.t_travel.presentation.common.permission

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionHandler(
    private val onSinglePermissionGranted: (() -> Unit)? = null,
    private val onSinglePermissionDenied: (() -> Unit)? = null,
    private val onMultiplePermissionGrantedCallback: Map<String, (() -> Unit)> = emptyMap(),
    private val onMultiplePermissionDeniedCallback: Map<String, (() -> Unit)> = emptyMap()
) {
    private var activity: ComponentActivity? = null
    private var singlePermissionResult: ActivityResultLauncher<String>? = null
    private var multiplePermissionResult: ActivityResultLauncher<Array<String>>? = null

    fun initContracts(activity: ComponentActivity) {
        if (this.activity == null) {
            this.activity = activity
        }
        if (singlePermissionResult == null) {
            singlePermissionResult =
                this.activity?.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        onSinglePermissionGranted?.invoke()
                    } else {
                        onSinglePermissionDenied?.invoke()
                    }
                }
        }
        if (multiplePermissionResult == null) {
            multiplePermissionResult =
                this.activity?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    val deniedPermissions = mutableListOf<String>()
                    permissions.forEach { (permission, isGranted) ->
                        if (isGranted) {
                            onMultiplePermissionGrantedCallback[permission]?.invoke()
                        } else {
                            onMultiplePermissionDeniedCallback[permission]?.invoke()
                        }

                    }
                }
        }
    }

    fun requestSinglePermission(permission: String) {
        singlePermissionResult?.launch(permission)
    }

    fun requestMultiplePermission(permission: List<String>) {
        multiplePermissionResult?.launch(permission.toTypedArray())
    }
}