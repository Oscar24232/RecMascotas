package com.example.trabajoredsocial.Imagenes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trabajoredsocial.ViewModel.ImagenViewModel
import java.io.File

@Composable
fun CargarImagen(
    mainViewModel: ImagenViewModel = viewModel()
) {

    val context = LocalContext.current
    val imageFile = mainViewModel.imageFile.value

    // Cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageFile?.let { file ->
                mainViewModel.updateImageUri(Uri.fromFile(file))
            }
        } else {
            mainViewModel.updateImageUri(Uri.EMPTY)
        }
    }

    // Permisos cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file = File.createTempFile("CAM_",".jpg",context.cacheDir
            )
            mainViewModel.setImageFile(file)

            cameraLauncher.launch(
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    file
                )
            )
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Galería
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val tempFile = File.createTempFile("GAL_",".jpg",context.cacheDir
            )

            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            mainViewModel.setImageFile(tempFile)
            mainViewModel.updateImageUri(Uri.fromFile(tempFile))
        }
    }
}