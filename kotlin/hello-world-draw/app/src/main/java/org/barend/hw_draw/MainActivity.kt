package org.barend.hw_draw

import android.content.Context
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

        // Use layout change to detect any size change,
        // and to avoid creating the bitmap on creation (the views dimensions are still 0)
        imageView.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            if (v.width > 0 && v.height > 0) {
                imageView.setImageBitmap(createBitmap(v.width, v.height, listOf("Hello", "World"), Color.DKGRAY))
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
                    "the", "universe", "and", "everything.",
                    "And", "the", "answer", "is:", "42")))
        }
    }

    private fun createBitmap(width: Int, height: Int, texts : List<String>, canvasColor: Int = Color.YELLOW): Bitmap {

        val margin : Float = 50.0f

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawColor(canvasColor)

        val paint = Paint()

        // 1: Draw a simple rectangle

        paint.isAntiAlias = true
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10.0f

        canvas.drawRect(margin, margin, width - margin, height - margin, paint)

        // 2: Draw a simple circle
        paint.style = Paint.Style.FILL
        paint.color = Color.GREEN
        canvas.drawCircle(width * 0.6f, height * 0.75f, 25.0f, paint)

        // 3: Draw a filled triangle somewhere at the bottom left
        paint.color = Color.RED
        val path = Path().apply {
            moveTo(width * 0.15f, height * 0.82f)
            lineTo(width * 0.2f, height * 0.75f)
            lineTo(width * 0.25f, height * 0.82f)
            close()
        }
        canvas.drawPath(path, paint)

        // 4: Draw bitmaps
        // To get a bitmap, there are many options:
        //   * refer to the other HelloWorld project "retrieve-draw-image" (which gets it from the Internet)
        //   * include it as an VectorAsset (or a ImageAsset)
        //     To do that, go to "res", right click, select "New", select "VectorAsset"
        //   * include it as a plain JPG or PNG
        //     To do that, drag any JPG or PNG file into the "res/drawable" folder
        //     I used: https://icons8.com/icon/8n428k8C7l0w/pet-commands-follow
        canvas.drawBitmap(createBitmapFromDrawable(R.drawable.cat_sample), width * 0.3f, height * 0.7f, paint)
        canvas.drawBitmap(createBitmapFromDrawable(R.drawable.ic_person, 4), width * 0.7f, height * 0.7f, paint)

        // 5: Draw the specified strings
        drawText(canvas, margin * 2.0f, width - margin * 2.0f, texts)

        return bitmap
    }

    // Function that converts a vector asset, or a bitmap asset, to a bitmap
    // Adapted from: https://www.geeksforgeeks.org/how-to-convert-a-vector-to-bitmap-in-android/
    private fun createBitmapFromDrawable(drawableId: Int, multiplier : Int = 1): Bitmap {
        val drawable = ContextCompat.getDrawable(this@MainActivity, drawableId)
        val bitmap = Bitmap.createBitmap(
            multiplier * drawable!!.intrinsicWidth,
            multiplier * drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
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
