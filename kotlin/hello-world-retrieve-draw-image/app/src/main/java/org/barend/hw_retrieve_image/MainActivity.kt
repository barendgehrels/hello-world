package org.barend.hw_retrieve_image

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.barend.hw_retrieve_image.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textView : TextView = binding.textViewSample
        val button : Button = binding.buttonGetImage
        button.setOnClickListener {
            textView.text = "You clicked!"
        }
    }

}