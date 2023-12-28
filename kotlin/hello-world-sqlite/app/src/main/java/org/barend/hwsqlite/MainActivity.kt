package org.barend.hwsqlite

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.barend.hwsqlite.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    private val FREQUENCY_DB_NAME = "word_frequency_list.db"

    private val db = SQLiteWritableDatabase(this@MainActivity, Translation.DATABASE_NAME,
        arrayOf("""
            create table if not exists ${Translation.TABLE_TRANSLATIONS}
            (id integer primary key autoincrement, 
            ${Translation.COLUMN_ENGLISH} text, 
            ${Translation.COLUMN_FRENCH} text,
            ${Translation.COLUMN_LEVEL} int)
            """))

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        Log.i("MYDEBUG", "database: ${this.getDatabasePath(Translation.DATABASE_NAME)}")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddWord.setOnClickListener{  add() }
        binding.buttonList.setOnClickListener { list() }
        binding.buttonFrequency.setOnClickListener { listFrequencies() }
    }
    private fun textClear()
    {
        binding.textFirst.text = ""
        binding.textSecond.text = ""
        binding.textLevelRank.text = ""
    }

    private fun editClear()
    {
        binding.editEnglish.text.clear()
        binding.editFrench.text.clear()
        binding.editLevel.text.clear()
    }

    fun getTranslation() : ContentValues
    {
        val values = ContentValues()
        values.put(Translation.COLUMN_ENGLISH, binding.editEnglish.text.toString())
        values.put(Translation.COLUMN_FRENCH, binding.editFrench.text.toString())
        values.put(Translation.COLUMN_LEVEL, binding.editLevel.text.toString())
        return values
    }

    private fun add()
    {
        db.insert(Translation.TABLE_TRANSLATIONS, getTranslation())

        editClear()
    }

    private fun list()
    {
        textClear()

        db.select("select ${Translation.COLUMN_ENGLISH},${Translation.COLUMN_FRENCH},${Translation.COLUMN_LEVEL} from ${Translation.TABLE_TRANSLATIONS}")
        {
            binding.textFirst.append("${it.getString(0)}\n")
            binding.textSecond.append("${it.getString(1)}\n")
            binding.textLevelRank.append("${it.getInt(2)}\n")
        }
    }

    private fun listRange()
    {
        textClear()

        db.select("""
            select ${Translation.COLUMN_ENGLISH},${Translation.COLUMN_FRENCH},${Translation.COLUMN_LEVEL}
            from ${Translation.TABLE_TRANSLATIONS} 
            where ${Translation.COLUMN_LEVEL} >= ? and ${Translation.COLUMN_LEVEL} <= ?
            """, arrayOf<Long>(2, 4))
        {
            binding.textFirst.append("${it.getString(0)}\n")
            binding.textSecond.append("${it.getString(1)}\n")
            binding.textLevelRank.append("${it.getInt(2)}\n")
        }
    }

    private fun listFrequencies()
    {
        textClear()

        // This piece of code opens a database, executes a query and displays the results,
        // all in pure Kotlin code and without any helper object.
        // In this example, this database comes from the assets, and it is copied on first usage.
        try {
            copyDbIfNew(FREQUENCY_DB_NAME)

            val mydb = openOrCreateDatabase(getDatabasePath(FREQUENCY_DB_NAME).toString(), Context.MODE_PRIVATE, null);
            val cursor = mydb.rawQuery("select rank,spanish,english from wf_list limit 10", null)
            while (cursor.moveToNext())
            {
                binding.textLevelRank.append("${cursor.getInt(0)}\n")
                binding.textFirst.append("${cursor.getString(1)}\n")
                binding.textSecond.append("${cursor.getString(2)}\n")
            }
            cursor.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun copyDbIfNew(databaseName : String) {
        val dbName = getDatabasePath(databaseName).toString()
        val outfile = File(dbName)
        if (!outfile.exists())
        {
            Log.i("MYDEBUG", "copy database from assets to: ${dbName}")
            val myInput = assets.open(databaseName)
            copyStream(myInput, outfile)
            myInput.close()
        }
    }
}
