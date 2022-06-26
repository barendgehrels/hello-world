package org.barend.hw_draw

import android.graphics.*
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.barend.hw_draw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageView : ImageView = binding.imageView

        // Use layout change to detect any size change
        imageView.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            if (v.width > 0 && v.height > 0) {
                imageView.setImageBitmap(createBitmap(v.width, v.height, listOf("Hello", "World"), Color.CYAN))
            }
        }

        binding.buttonLeft.setOnClickListener {
            imageView.setImageBitmap(createBitmap(imageView.width, imageView.height,
                listOf("You", "clicked", "the", "left", "button", "!")))
        }
        binding.buttonRight.setOnClickListener {
            imageView.setImageBitmap(createBitmap(imageView.width, imageView.height,
                listOf("You", "clicked", "the", "right", "button!",
                    "Therefore", "you", "get", "finally", "the", "answer",
                    "to", "the", "ultimate", "question", "of", "life,",
                    "The", "universe", "and", "everything.",
                    "And", "the", "answer", "is:", "42")))
        }
    }

    private fun createBitmap(width: Int, height: Int, texts : List<String>, canvasColor: Int = Color.YELLOW): Bitmap {

        val margin : Float = 50.0f

        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        canvas.drawColor(canvasColor)

        val paint = Paint()

        //canvas.drawBitmap(map, 0.0f, 0.0f, paint)

        // 1: Draw a simple rectangle
        val path = Path().apply {
            moveTo(margin, margin)
            lineTo(margin, height - margin)
            lineTo(width - margin, height - margin)
            lineTo(width - margin, margin)
            close()
        }

        paint.isAntiAlias = true
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10.0f

        canvas.drawPath(path, paint)

        // 2: Draw a simple circle
        paint.style = Paint.Style.FILL
        paint.color = Color.MAGENTA
        canvas.drawCircle(width / 2.0f, height / 2.0f, 20.0f, paint)

        // 3: Draw the specified strings
        drawText(canvas, margin * 2.0f, width - margin * 2.0f, texts)

        return bitmap
    }

    // This more complex method contains some logic for word wrapping,
    // and to draw in alternating colors
    private fun drawText(canvas : Canvas, textMargin : Float, maxWidth: Float, texts : List<String>) {

        val paint = Paint().apply {
            setTextSize(64.0f)
            setTypeface(Typeface.DEFAULT_BOLD)
        }

        // Somehow, measuring a space " " results in dimensions (0,0)
        // So get it from the "X", (though it looks a bit too wide, make it a bit smaller)
        var charRect = Rect()
        paint.getTextBounds("X", 0, 1, charRect)
        val spaceWidth = charRect.width() * 0.6f
        val lineHeight = charRect.height() * 1.5f

        var x : Float = textMargin
        var y : Float = textMargin

        for ((c, s) in texts.withIndex()) {

            // Get color in different ways, for demonstration purposes
            when {
                c % 3 == 0 ->paint.color = Color.valueOf(1.0f, 0.4f, 0.0f).toArgb() // Orange
                c % 3 == 1 ->paint.color = Color.parseColor("#0000EE") // Blue
                else -> paint.color = ContextCompat.getColor(this@MainActivity, R.color.mygreen);
            }

            var textRect = Rect()
            paint.getTextBounds(s, 0, s.length, textRect)

            if (x + textRect.width() >= maxWidth) {
                // Take care of word wraps
                x = textMargin
                y = y + lineHeight
            }

            // Draw the text. It's aligned from the top, therefore add the height of the measured "X"
            canvas.drawText(s, x, y + charRect.height(), paint)
            x += textRect.width() + spaceWidth
        }
    }
}
