package org.barend.hw_retrieve_image

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.barend.hw_retrieve_image.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textView : TextView = binding.textViewSample
        val button : Button = binding.buttonGetImage
        val imageView : ImageView = binding.imageView

        button.setOnClickListener {
            textView.text = getString(R.string.one_moment)
            asyncRetrieveImage(imageView, textView)
        }
    }

    private fun asyncRetrieveImage(imageView : ImageView, textView : TextView) {
        // Retrieve an URL should be done in another thread
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            retrieveImage(imageView, textView)
        }
    }

    private fun retrieveImage(imageView : ImageView, textView : TextView) {
        val imageURL = "https://raw.githubusercontent.com/barendgehrels/hello-world/main/data/cat_in_amsterdam.jpg"

        try {
            val bitmap = BitmapFactory.decodeStream(java.net.URL(imageURL).openStream())

            // Because we are inside an activity, just call runOnUiThread.
            // Else it should be wrapped inside Handler(Looper.getMainLooper()).post { }
            runOnUiThread {  imageView.setImageBitmap(bitmap)
                if (bitmap != null) {
                    textView.text = "Image retrieved ${bitmap.width} x ${bitmap.height}"
                } else {
                    textView.text = getString(R.string.notify_no_image)
                }
            }
        }
        catch(e: FileNotFoundException) {
            e.printStackTrace()
            runOnUiThread { textView.text = "Not found: ${e.message}" }
        }
        catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread { textView.text = "Exception: ${e.message}" }
        }
    }
}
