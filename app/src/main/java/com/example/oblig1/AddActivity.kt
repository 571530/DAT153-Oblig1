package com.example.oblig1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.app.Activity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.IOException


class AddActivity : AppCompatActivity() {
    private val GET_FROM_GALLERY = 3
    private val REQUEST_IMAGE_CAPTURE = 1
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
    }


    fun uploadImage(view: View) {
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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                updateImage()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            bitmap = data?.extras?.get("data") as Bitmap
            updateImage()
        }
    }

    private fun updateImage() {
        val imageView = findViewById<ImageView>(R.id.uploadImage)
        imageView.setImageBitmap(bitmap)
    }

    fun addPerson(view: View) {
        val text = findViewById<TextView>(R.id.editText).text

        when {
            bitmap == null -> {
                val toast = Toast.makeText(this, "Missing image!!", Toast.LENGTH_SHORT)
                toast.show()
            }
            text.isEmpty() -> {
                val toast = Toast.makeText(this, "MISSING TEXT!", Toast.LENGTH_SHORT)
                toast.show()
            }
            else -> {
                val person = Person(text.toString(), bitmap!!)
                PersonDatabase(application).add(person)
                resetForm()
                val toast = Toast.makeText(this, "$text added!!!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    fun dispatchTakePictureIntent(view: View) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun resetForm() {
        bitmap = null

        val imageView = findViewById<ImageView>(R.id.uploadImage)
        imageView.invalidate()
        imageView.setImageBitmap(null)

        val textView = findViewById<TextView>(R.id.editText)
        textView.text = ""
    }

}
