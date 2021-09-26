package com.marlon.businesscard.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.marlon.businesscard.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import androidx.core.content.FileProvider
import com.marlon.businesscard.BuildConfig


class Image {

    companion object {
        fun share(context:Context, card: View){
            val bitmap = getScreenShotFromView(card)
            bitmap?.let {
                saveMediaToStorage(context, bitmap)
            }
        }

        private fun getScreenShotFromView(card: View): Bitmap? {
            var screenshot: Bitmap? = null
            try {
                screenshot = Bitmap.createBitmap(card.measuredWidth, card.measuredHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(screenshot)
                card.draw(canvas)
            } catch (e:Exception) {
                Log.e("Error -> ", "Falha ao capturar imagem ${e.message}")
            }
            return screenshot
        }

        private fun saveMediaToStorage(context: Context, bitmap: Bitmap){
            val fileName = "${System.currentTimeMillis()}.jpg"
            var fileOutputStream: OutputStream? = null
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                Log.e("ERROR -> ", "PRIMEIRO IF")
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    var imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fileOutputStream = imageUri?.let {
                        shareIntent(context, imageUri)
                        resolver.openOutputStream(it)
                    }
                }
            } else {
                Log.e("ERROR -> ", "SEGUNDO IF")
                //Devices rodando > Q
                val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, fileName)
                val uri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID.toString() + ".provider",
                    image
                )
                shareIntent(context, uri)
                fileOutputStream = FileOutputStream(image)
                fileOutputStream?.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    Toast.makeText(context, "Imagem capturada com sucesso", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun shareIntent(context: Context, imageUri: Uri) {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/jprg"
            }
            context.startActivity(
                Intent.createChooser(shareIntent, context.resources.getText(R.string.label_share))
            )
        }
    }


}