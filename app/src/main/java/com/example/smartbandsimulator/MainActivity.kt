package com.example.smartbandsimulator

import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // UI references
    private lateinit var heartRateInput: EditText
    private lateinit var heartRateStatus: TextView
    private lateinit var stepsInput: EditText
    private lateinit var stepsStatus: TextView
    private lateinit var bloodPressureInput: EditText
    private lateinit var bloodPressureStatus: TextView
    private lateinit var caloriesInput: EditText
    private lateinit var caloriesStatus: TextView
    private lateinit var sleepInput: EditText
    private lateinit var sleepStatus: TextView
    private lateinit var temperatureInput: EditText
    private lateinit var temperatureStatus: TextView

    // Chart for heart rate
    private lateinit var heartRateChart: LineChart
    private val heartRateEntries = mutableListOf<Entry>()
    private var heartRateIndex = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind views
        heartRateInput       = findViewById(R.id.heartRateInput)
        heartRateStatus      = findViewById(R.id.heartRateStatus)
        stepsInput           = findViewById(R.id.stepsInput)
        stepsStatus          = findViewById(R.id.stepsStatus)
        bloodPressureInput   = findViewById(R.id.bloodPressureInput)
        bloodPressureStatus  = findViewById(R.id.bloodPressureStatus)
        caloriesInput        = findViewById(R.id.caloriesInput)
        caloriesStatus       = findViewById(R.id.caloriesStatus)
        sleepInput           = findViewById(R.id.sleepInput)
        sleepStatus          = findViewById(R.id.sleepStatus)
        temperatureInput     = findViewById(R.id.temperatureInput)
        temperatureStatus    = findViewById(R.id.temperatureStatus)

        // Chart setup
        heartRateChart = findViewById(R.id.heartRateChart)
        setupHeartRateChart()

        // Generate buttons
        findViewById<Button>(R.id.generateHeartRate).setOnClickListener {
            val hr = Random.nextInt(60, 100)
            heartRateInput.setText(hr.toString())
            updateHeartRateStatus(hr)
            updateHeartRateChart(hr.toFloat())
        }
        findViewById<Button>(R.id.generateSteps).setOnClickListener {
            val steps = Random.nextInt(0, 10000)
            stepsInput.setText(steps.toString())
            updateStepsStatus(steps)
        }
        findViewById<Button>(R.id.generateBloodPressure).setOnClickListener {
            val sys = Random.nextInt(100, 140)
            val dia = Random.nextInt(60, 90)
            bloodPressureInput.setText("$sys/$dia")
            updateBPStatus(sys, dia)
        }
        findViewById<Button>(R.id.generateCalories).setOnClickListener {
            val cal = Random.nextInt(0, 500)
            caloriesInput.setText(cal.toString())
            updateCaloriesStatus(cal)
        }
        findViewById<Button>(R.id.generateSleep).setOnClickListener {
            val sl = Random.nextDouble(6.0, 9.0)
            sleepInput.setText("%.1f".format(sl))
            updateSleepStatus(sl)
        }
        findViewById<Button>(R.id.generateTemperature).setOnClickListener {
            val temp = Random.nextDouble(36.5, 37.5)
            temperatureInput.setText("%.1f".format(temp))
            updateTempStatus(temp)
        }

        // Manual "Done" actions
        heartRateInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.text.toString().toIntOrNull()?.let { updateHeartRateStatus(it) }
                true
            } else false
        }
        stepsInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.text.toString().toIntOrNull()?.let { updateStepsStatus(it) }
                true
            } else false
        }
        bloodPressureInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.text.toString().split("/").takeIf { it.size == 2 }?.let {
                    it[0].toIntOrNull()?.let { sys ->
                        it[1].toIntOrNull()?.let { dia ->
                            updateBPStatus(sys, dia)
                        }
                    }
                }
                true
            } else false
        }
        caloriesInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.text.toString().toIntOrNull()?.let { updateCaloriesStatus(it) }
                true
            } else false
        }
        sleepInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.text.toString().toDoubleOrNull()?.let { updateSleepStatus(it) }
                true
            } else false
        }
        temperatureInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.text.toString().toDoubleOrNull()?.let { updateTempStatus(it) }
                true
            } else false
        }
    }

    // Status update functions
    private fun updateHeartRateStatus(hr: Int) {
        val status = classifyHR(hr)
        heartRateStatus.text = "Status: ${status.name}"
        heartRateStatus.setTextColor(
            when (status) {
                HRStatus.LOW    -> Color.BLUE
                HRStatus.NORMAL -> Color.GREEN
                HRStatus.HIGH   -> Color.RED
            }
        )
        if (status != HRStatus.NORMAL) {
            Toast.makeText(this, "Atenție: ritm cardiac $status!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateStepsStatus(steps: Int) {
        val status = classifySteps(steps)
        stepsStatus.text = "Status: ${status.name}"
        stepsStatus.setTextColor(
            when (status) {
                StepsStatus.SEDENTARY    -> Color.RED
                StepsStatus.LIGHT_ACTIVE -> Color.YELLOW
                StepsStatus.ACTIVE       -> Color.GREEN
                StepsStatus.VERY_ACTIVE  -> Color.MAGENTA
            }
        )
    }
    private fun updateBPStatus(sys: Int, dia: Int) {
        val status = classifyBloodPressure(sys, dia)
        bloodPressureStatus.text = "Status: ${status.name}"
        bloodPressureStatus.setTextColor(
            when (status) {
                BPStatus.LOW            -> Color.BLUE
                BPStatus.NORMAL         -> Color.GREEN
                BPStatus.PREHYPERTENSION-> Color.YELLOW
                BPStatus.HYPERTENSION   -> Color.RED
            }
        )
    }
    private fun updateCaloriesStatus(cal: Int) {
        val status = classifyCalories(cal)
        caloriesStatus.text = "Status: ${status.name}"
        caloriesStatus.setTextColor(Color.GREEN) // toate în verde
    }
    private fun updateSleepStatus(hours: Double) {
        val status = classifySleep(hours)
        sleepStatus.text = "Status: ${status.name}"
        sleepStatus.setTextColor(
            when (status) {
                SleepStatus.INSUFFICIENT -> Color.RED
                SleepStatus.ADEQUATE     -> Color.GREEN
                SleepStatus.EXCESS       -> Color.BLUE
            }
        )
    }
    private fun updateTempStatus(temp: Double) {
        val status = classifyTemperature(temp)
        temperatureStatus.text = "Status: ${status.name}"
        temperatureStatus.setTextColor(
            when (status) {
                TempStatus.NORMAL -> Color.GREEN
                TempStatus.FEVER  -> Color.RED
            }
        )
        if (status == TempStatus.FEVER) {
            Toast.makeText(this, "Atenție: febră!", Toast.LENGTH_SHORT).show()
        }
    }

    // Chart helpers for heart rate
    private fun setupHeartRateChart() {
        heartRateChart.description.isEnabled = false
        heartRateChart.setTouchEnabled(false)
        heartRateChart.setPinchZoom(false)
        heartRateChart.xAxis.isEnabled = false
        heartRateChart.axisRight.isEnabled = false
        heartRateChart.axisLeft.axisMinimum = 40f
        heartRateChart.axisLeft.axisMaximum = 180f
    }
    private fun updateHeartRateChart(value: Float) {
        heartRateEntries.add(Entry(heartRateIndex++, value))
        val set = LineDataSet(heartRateEntries, "Ritm Cardiac").apply {
            setDrawValues(false)
            setDrawCircles(false)
            color = Color.RED
        }
        heartRateChart.data = LineData(set)
        heartRateChart.invalidate()
    }
}
