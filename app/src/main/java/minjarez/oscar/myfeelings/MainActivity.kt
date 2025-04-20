package minjarez.oscar.myfeelings

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import minjarez.oscar.myfeelings.databinding.ActivityMainBinding
import minjarez.oscar.myfeelings.utilities.CustomBarDrawable
import minjarez.oscar.myfeelings.utilities.CustomCircleDrawable
import minjarez.oscar.myfeelings.utilities.Emotion
import minjarez.oscar.myfeelings.utilities.JsonFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var jsonFile: JsonFile? = null
    var veryHappy = 0.0f
    var happy = 0.0f
    var neutral = 0.0f
    var sad = 0.0f
    var verySad = 0.0f
    var data: Boolean = false
    var list = ArrayList<Emotion>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.jsonFile = JsonFile()
        this.fetchingData()
        if (!data) {
            var emotions = ArrayList<Emotion>()
            var background = CustomCircleDrawable(this, emotions)
            this.binding.graph.background = background
            this.binding.graphVeryHappy.background = CustomBarDrawable(this, Emotion("Muy feliz", 0.0f, R.color.mustard, this.veryHappy))
            this.binding.graphHappy.background = CustomBarDrawable(this, Emotion("Happy", 0.0f, R.color.orange, this.happy))
            this.binding.graphNeutral.background = CustomBarDrawable(this, Emotion("Neutral", 0.0f, R.color.greenie, this.neutral))
            this.binding.graphSad.background = CustomBarDrawable(this, Emotion("Sad", 0.0f, R.color.blue, this.sad))
            this.binding.graphVerySad.background = CustomBarDrawable(this, Emotion("Very sad", 0.0f, R.color.mustard, this.verySad))
        } else {
            this.updateGraph()
            this.mostIcon()
        }
        this.binding.saveButton.setOnClickListener {
            this.save()
        }
        this.binding.veryHappyButton.setOnClickListener {
            this.veryHappy++
            this.mostIcon()
            this.updateGraph()
        }
        this.binding.happyButton.setOnClickListener {
            this.happy++
            this.mostIcon()
            this.updateGraph()
        }
        this.binding.neutralButton.setOnClickListener {
            this.neutral++
            this.mostIcon()
            this.updateGraph()
        }
        this.binding.sadButton.setOnClickListener {
            this.sad++
            this.mostIcon()
            this.updateGraph()
        }
        this.binding.verySadButton.setOnClickListener {
            this.verySad++
            this.mostIcon()
            this.updateGraph()
        }
    }

    fun fetchingData() {
        try {
            var json: String = this.jsonFile?.getData(this) ?: ""
            if (json != "") {
                this.data = true
                var jsonArray = JSONArray(json)
                this.list = parseJson(jsonArray)
                for (item in this.list) {
                    when (item.name) {
                        "Very happy" -> this.veryHappy = item.total
                        "Happy" -> this.happy = item.total
                        "Neutral" -> this.neutral = item.total
                        "Sad" -> this.sad = item.total
                        "Very sad" -> this.verySad = item.total
                    }
                }
            } else {
                this.data = false
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun mostIcon() {
        when {
            this.happy > this.veryHappy && this.happy > this.neutral && this.happy > this.sad && this.happy > this.verySad -> {
                this.binding.icon.setImageResource(R.drawable.ic_happy)
            }
            this.veryHappy > this.happy && this.veryHappy > this.neutral && this.veryHappy > this.sad && this.veryHappy > this.verySad -> {
                this.binding.icon.setImageResource(R.drawable.ic_veryhappy)
            }
            this.neutral > this.veryHappy && this.neutral > this.happy && this.neutral > this.sad && this.neutral > this.verySad -> {
                this.binding.icon.setImageResource(R.drawable.ic_neutral)
            }
            this.sad > this.happy && this.sad > this.neutral && this.sad > this.veryHappy && this.sad > this.verySad -> {
                this.binding.icon.setImageResource(R.drawable.ic_sad)
            }
            this.verySad > this.happy && this.verySad > this.neutral && this.verySad > this.sad && this.verySad > this.veryHappy -> {
                this.binding.icon.setImageResource(R.drawable.ic_verysad)
            }
        }
    }

    fun updateGraph() {
        val total = this.veryHappy + this.happy + this.neutral + this.verySad + this.sad
        var pVH = (this.veryHappy * 100 / total)
        var pH = (this.happy * 100 / total)
        var pN = (this.neutral * 100 / total)
        var pS = (this.sad * 100 / total)
        var pVS = (this.verySad * 100 / total)
        Log.d("percentages", "very happy $pVH")
        Log.d("percentages", "happy $pH")
        Log.d("percentages", "neutral $pN")
        Log.d("percentages", "sad $pS")
        Log.d("percentages", "very sad $pVS")
        this.list.clear()
        this.list.add(Emotion("Very happy", pVH, R.color.mustard, this.veryHappy))
        this.list.add(Emotion("Happy", pH, R.color.orange, this.happy))
        this.list.add(Emotion("Neutral", pN, R.color.greenie, this.neutral))
        this.list.add(Emotion("Sad", pS, R.color.blue, this.sad))
        this.list.add(Emotion("Very sad", pVS, R.color.deepBlue, this.verySad))
        val background = CustomCircleDrawable(this, this.list)
        this.binding.graphVeryHappy.background = CustomBarDrawable(this, Emotion("Muy feliz", pVH, R.color.mustard, this.veryHappy))
        this.binding.graphHappy.background = CustomBarDrawable(this, Emotion("Feliz", pH, R.color.orange, this.happy))
        this.binding.graphNeutral.background = CustomBarDrawable(this, Emotion("Neutral", pN, R.color.greenie, this.neutral))
        this.binding.graphSad.background = CustomBarDrawable(this, Emotion("Triste", pS, R.color.blue, this.sad))
        this.binding.graphVerySad.background = CustomBarDrawable(this, Emotion("Muy triste", pVS, R.color.deepBlue, this.verySad))
        this.binding.graph.background = background
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emotion> {
        var list = ArrayList<Emotion>()
        for (i in 0 until jsonArray.length()) {
            try {
                val name = jsonArray.getJSONObject(i).getString("name")
                val percentage = jsonArray.getJSONObject(i).getString("percentage").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getString("total").toFloat()
                val emotion = Emotion(name, percentage, color, total)
                list.add(emotion)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return list
    }

    fun save() {
        var jsonArray = JSONArray()
        var o = 0
        for (item in this.list) {
            Log.d("Objects", item.toString())
            var j = JSONObject()
            j.put("name", item.name)
            j.put("percentage", item.percentage)
            j.put("color", item.color)
            j.put("total", item.total)
            jsonArray.put(o, j)
            o++
        }
        this.jsonFile?.saveData(this, jsonArray.toString())
        Toast.makeText(this, "Saved data", Toast.LENGTH_SHORT).show()
    }
}