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

    // Enums and classification functions
    enum class HRStatus { LOW, NORMAL, HIGH }
    private fun classifyHR(value: Int): HRStatus = when {
        value < 60    -> HRStatus.LOW
        value <= 100  -> HRStatus.NORMAL
        else          -> HRStatus.HIGH
    }

    enum class StepsStatus { SEDENTARY, LIGHT_ACTIVE, ACTIVE, VERY_ACTIVE }
    private fun classifySteps(value: Int): StepsStatus = when {
        value < 2000      -> StepsStatus.SEDENTARY
        value <= 5000     -> StepsStatus.LIGHT_ACTIVE
        value <= 8000     -> StepsStatus.ACTIVE
        else              -> StepsStatus.VERY_ACTIVE
    }

    enum class BPStatus { LOW, NORMAL, PREHYPERTENSION, HYPERTENSION }
    private fun classifyBloodPressure(sys: Int, dia: Int): BPStatus = when {
        sys < 100 && dia < 60                     -> BPStatus.LOW
        sys <= 120 && dia <= 80                   -> BPStatus.NORMAL
        sys <= 140 && dia <= 90                   -> BPStatus.PREHYPERTENSION
        else                                       -> BPStatus.HYPERTENSION
    }

    enum class CalStatus { LOW, NORMAL, HIGH }
    private fun classifyCalories(value: Int): CalStatus = when {
        value < 100    -> CalStatus.LOW
        value <= 300   -> CalStatus.NORMAL
        else           -> CalStatus.HIGH
    }

    enum class SleepStatus { INSUFFICIENT, ADEQUATE, EXCESS }
    private fun classifySleep(value: Double): SleepStatus = when {
        value < 6.0    -> SleepStatus.INSUFFICIENT
        value <= 9.0   -> SleepStatus.ADEQUATE
        else           -> SleepStatus.EXCESS
    }

    enum class TempStatus { LOW, NORMAL, FEVER }
    private fun classifyTemperature(value: Double): TempStatus = when {
        value < 36.5   -> TempStatus.LOW
        value <= 37.5  -> TempStatus.NORMAL
        else           -> TempStatus.FEVER
    }

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

        // Generate buttons with extended ranges for testing
        findViewById<Button>(R.id.generateHeartRate).setOnClickListener {
            val hr = Random.nextInt(40, 140)
            heartRateInput.setText(hr.toString())
            updateHeartRateStatus(hr)
            updateHeartRateChart(hr.toFloat())
        }
        findViewById<Button>(R.id.generateSteps).setOnClickListener {
            val steps = Random.nextInt(0, 12000)
            stepsInput.setText(steps.toString())
            updateStepsStatus(steps)
        }
        findViewById<Button>(R.id.generateBloodPressure).setOnClickListener {
            val sys = Random.nextInt(80, 160)
            val dia = Random.nextInt(50, 100)
            bloodPressureInput.setText("$sys/$dia")
            updateBPStatus(sys, dia)
        }
        findViewById<Button>(R.id.generateCalories).setOnClickListener {
            val cal = Random.nextInt(0, 600)
            caloriesInput.setText(cal.toString())
            updateCaloriesStatus(cal)
        }
        findViewById<Button>(R.id.generateSleep).setOnClickListener {
            val sl = Random.nextDouble(4.0, 12.0)
            sleepInput.setText("%.1f".format(sl))
            updateSleepStatus(sl)
        }
        findViewById<Button>(R.id.generateTemperature).setOnClickListener {
            val temp = Random.nextDouble(35.0, 40.0)
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

    // Status update functions with real recommendations

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
        when (status) {
            HRStatus.LOW -> Toast.makeText(
                this,
                "Ritm prea scăzut ($hr bpm). Recomandare: odihnește-te, bea apă și, dacă persistă, consultă un medic.",
                Toast.LENGTH_LONG
            ).show()
            HRStatus.HIGH -> Toast.makeText(
                this,
                "Ritm prea ridicat ($hr bpm). Recomandare: fă respirații lente, evită efortul intens și, dacă nu scade după 5-10 min, cere sfat medical.",
                Toast.LENGTH_LONG
            ).show()
            else -> { }
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
        when (status) {
            StepsStatus.SEDENTARY -> Toast.makeText(
                this,
                "Mai puțini de 2.000 pași/zi. Recomandare: fă o plimbare de 20–30 minute azi.",
                Toast.LENGTH_LONG
            ).show()
            StepsStatus.VERY_ACTIVE -> Toast.makeText(
                this,
                "Foarte activ azi (>8.000 pași)! Continuă ritmul și asigură-te că te hidratezi.",
                Toast.LENGTH_LONG
            ).show()
            else -> { }
        }
    }

    private fun updateBPStatus(sys: Int, dia: Int) {
        val status = classifyBloodPressure(sys, dia)
        bloodPressureStatus.text = "Status: ${status.name}"
        bloodPressureStatus.setTextColor(
            when (status) {
                BPStatus.LOW             -> Color.BLUE
                BPStatus.NORMAL          -> Color.GREEN
                BPStatus.PREHYPERTENSION -> Color.YELLOW
                BPStatus.HYPERTENSION    -> Color.RED
            }
        )
        when (status) {
            BPStatus.LOW -> Toast.makeText(
                this,
                "Tensiune prea mică ($sys/$dia). Recomandare: bea un pahar cu apă, stai jos, ridică-picioarele și monitorizează tensiunea. Dacă amețești, consultă un medic.",
                Toast.LENGTH_LONG
            ).show()
            BPStatus.HYPERTENSION -> Toast.makeText(
                this,
                "Tensiune prea mare ($sys/$dia)! Recomandare: evită sarea, relaxează-te și, dacă rămâne ridicată, cere ajutor medical.",
                Toast.LENGTH_LONG
            ).show()
            else -> { }
        }
    }

    private fun updateCaloriesStatus(cal: Int) {
        val status = classifyCalories(cal)
        caloriesStatus.text = "Status: ${status.name}"
        caloriesStatus.setTextColor(Color.GREEN)
        when (status) {
            CalStatus.LOW -> Toast.makeText(
                this,
                "Foarte puține calorii (<100 kcal). Recomandare: consumă o gustare sănătoasă (fructe, nuci).",
                Toast.LENGTH_LONG
            ).show()
            CalStatus.HIGH -> Toast.makeText(
                this,
                "Calorii ridicate (>300 kcal). Recomandare: echilibrează-ți mesele cu legume și proteine slabe, hidratează-te.",
                Toast.LENGTH_LONG
            ).show()
            else -> { }
        }
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
        when (status) {
            SleepStatus.INSUFFICIENT -> Toast.makeText(
                this,
                "Somn insuficient (<6h). Recomandare: încearcă să mergi devreme la culcare și să reduci ecranele înainte de somn.",
                Toast.LENGTH_LONG
            ).show()
            SleepStatus.EXCESS -> Toast.makeText(
                this,
                "Somn prea mult (>9h). Recomandare: adoptă o rutină de trezire constantă și expune-te la lumină naturală dimineața.",
                Toast.LENGTH_LONG
            ).show()
            else -> { }
        }
    }

    private fun updateTempStatus(temp: Double) {
        val status = classifyTemperature(temp)
        temperatureStatus.text = "Status: ${status.name}"
        temperatureStatus.setTextColor(
            when (status) {
                TempStatus.LOW    -> Color.BLUE
                TempStatus.NORMAL -> Color.GREEN
                TempStatus.FEVER  -> Color.RED
            }
        )
        when (status) {
            TempStatus.LOW -> Toast.makeText(
                this,
                "Temperatură sub normal (<36.5°C). Recomandare: încălzește-te ușor, bea lichide calde și monitorizează.",
                Toast.LENGTH_LONG
            ).show()
            TempStatus.FEVER -> Toast.makeText(
                this,
                "Febră (>37.5°C). Recomandare: odihnește-te, bea lichide, poți lua paracetamol și, dacă febra persistă, consultă un medic.",
                Toast.LENGTH_LONG
            ).show()
            else -> { }
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
