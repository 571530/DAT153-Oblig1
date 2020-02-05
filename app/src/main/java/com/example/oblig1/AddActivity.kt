package com.example.oblig1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import java.io.FileNotFoundException
import java.io.IOException
import androidx.room.util.CursorUtil.getColumnIndex
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class AddActivity : AppCompatActivity() {
    private val GET_FROM_GALLERY = 3
    private val REQUEST_IMAGE_CAPTURE = 1
    private var picturePath: String? = null
    private lateinit var viewModel: PersonViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        viewModel = ViewModelProvider(this).get(PersonViewModel::class.java)
    }

    fun uploadImage(view: View) {
        requirePermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            ), GET_FROM_GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(
                selectedImage,
                filePathColumn, null, null, null
            )
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            picturePath = cursor.getString(columnIndex)
            cursor.close()
            updateImage()
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            updateImage()
        }
    }

    private fun updateImage() {
        val imageView = findViewById<ImageView>(R.id.uploadImage)
        Glide
            .with(this)
            .load(picturePath)
            .into(imageView)
    }

    fun addPerson(view: View) {
        val text = findViewById<TextView>(R.id.editText).text

        when {
            picturePath == null -> {
                val toast = Toast.makeText(this, "Missing image!!", Toast.LENGTH_SHORT)
                toast.show()
            }
            text.isEmpty() -> {
                val toast = Toast.makeText(this, "MISSING TEXT!", Toast.LENGTH_SHORT)
                toast.show()
            }
            else -> {
                val p = Person(name = text.toString(), filepath = picturePath!!)

                viewModel.insert(p)

                resetForm()
                val toast = Toast.makeText(this, "$text added!!!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            picturePath = absolutePath
        }
    }

    fun takePicture(view: View) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? =
                    try {
                        createImageFile()
                    }
                    catch (ex: Exception) {
                        null
                    }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun resetForm() {
        picturePath = null

        val imageView = findViewById<ImageView>(R.id.uploadImage)
        imageView.invalidate()
        imageView.setImageBitmap(null)

        val textView = findViewById<TextView>(R.id.editText)
        textView.text = ""
    }
}
