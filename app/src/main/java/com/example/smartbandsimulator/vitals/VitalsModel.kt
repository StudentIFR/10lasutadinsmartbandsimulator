package com.example.smartbandsimulator.vitals

import java.util.Date
import kotlin.random.Random

// Status enums for fiecare semnal vital
enum class HRStatus { LOW, NORMAL, HIGH }
enum class BPStatus { LOW, NORMAL, HIGH }
enum class SpO2Status { LOW, NORMAL }

// Funcții de clasificare
fun classifyHR(hr: Int): HRStatus = when {
    hr < 60 -> HRStatus.LOW
    hr <= 100 -> HRStatus.NORMAL
    else -> HRStatus.HIGH
}

fun classifyBP(systolic: Int, diastolic: Int): BPStatus = when {
    systolic < 90 || diastolic < 60 -> BPStatus.LOW
    systolic <= 120 && diastolic <= 80 -> BPStatus.NORMAL
    else -> BPStatus.HIGH
}

fun classifySpO2(spo2: Int): SpO2Status =
    if (spo2 < 95) SpO2Status.LOW else SpO2Status.NORMAL

// Harta recomandărilor pentru cele 18 combinații
object RecommendationMap {
    private val map: Map<Triple<HRStatus, BPStatus, SpO2Status>, String> = mapOf(
        Triple(HRStatus.LOW, BPStatus.LOW, SpO2Status.LOW) to
                "Urgent! Posibil șoc: sună/consultați imediat un medic, verificați poziția pacientului (culcat cu picioarele ridicate), administrați oxigen dacă există.",
        Triple(HRStatus.LOW, BPStatus.LOW, SpO2Status.NORMAL) to
                "Risc de hipovolemie sau bloc AV: hidratare orală/IV, verifică medicamente (beta-blocante), consult cardiolog.",
        Triple(HRStatus.LOW, BPStatus.NORMAL, SpO2Status.LOW) to
                "Verifică sursa hipoxiei (plămâni vs. inimă): oxigenoterapie, EKG, consult pneumolog/cardiolog.",
        Triple(HRStatus.LOW, BPStatus.NORMAL, SpO2Status.NORMAL) to
                "Bradicardie izolată: monitorizare ambulatorie; dacă apare amețeală sau sincopă, dozare medicamente, eventual pacemaker.",
        Triple(HRStatus.LOW, BPStatus.HIGH, SpO2Status.LOW) to
                "Hipoxie + HTA + bradicardie: posibilă disfuncție baroreceptor; internare, monitorizare continuă, investigații complexe (ECG, ecografie cardiacă).",
        Triple(HRStatus.LOW, BPStatus.HIGH, SpO2Status.NORMAL) to
                "Sindrom de hipertensiune intracraniană? (Cushing) sau medicație excesivă; măsoară-ți tensiunea, revizuiește tratamentul antihipertensiv.",
        Triple(HRStatus.NORMAL, BPStatus.LOW, SpO2Status.LOW) to
                "Monitorizează, ai grijă la semne de hipovolemie; hidratare și măsurători repetate, eventual consult; dacă persistă, investigații de volum sangvin.",
        Triple(HRStatus.NORMAL, BPStatus.LOW, SpO2Status.NORMAL) to
                "Hipotensiune ortostatică posibilă: evită ridicat brusc, poartă ciorapi compresivi, rehidratează-te.",
        Triple(HRStatus.NORMAL, BPStatus.NORMAL, SpO2Status.LOW) to
                "Izolat SpO₂ scăzut: asigură-te că senzorul e bine poziționat, apoi investighează funcția pulmonară (spirometrie).",
        Triple(HRStatus.NORMAL, BPStatus.NORMAL, SpO2Status.NORMAL) to
                "Totul în limite normale – recomandare: menține un stil de viață activ, hidratează-te, repetă măsurarea la 24h.",
        Triple(HRStatus.NORMAL, BPStatus.HIGH, SpO2Status.LOW) to
                "Hipertensiune + hipoxie: evaluare cardiopulmonară de urgență, oxigen, antihipertensive intravenoase dacă e necesar, internare.",
        Triple(HRStatus.NORMAL, BPStatus.HIGH, SpO2Status.NORMAL) to
                "Hipertensiune izolată: modificări stil de viață (dietă săracă în sare, exerciții), medicație anti-HTA conform ghid.",
        Triple(HRStatus.HIGH, BPStatus.LOW, SpO2Status.LOW) to
                "Șoc mixt: urgență medicală, monitorizare intensivă, oxigen, vasopresoare/fluide IV.",
        Triple(HRStatus.HIGH, BPStatus.LOW, SpO2Status.NORMAL) to
                "Tahicardie compensatorie pentru hipovolemie: hidratare IV, investigație cauză pierdere de sânge/fluide.",
        Triple(HRStatus.HIGH, BPStatus.NORMAL, SpO2Status.LOW) to
                "Tahicardie + hipoxie: suspiciune de embolie pulmonară, pneumonie; oxigen, D-dimer, CT-angiografie pulmonară.",
        Triple(HRStatus.HIGH, BPStatus.NORMAL, SpO2Status.NORMAL) to
                "Tahicardie izolată: hidratare, reduce cofeina, verifică tiroida; ECG dacă persistă > 24h.",
        Triple(HRStatus.HIGH, BPStatus.HIGH, SpO2Status.LOW) to
                "Criză hipertensivă cu hipoxie: internare, oxigenoterapie, antihipertensive IV, monitorizare continuă.",
        Triple(HRStatus.HIGH, BPStatus.HIGH, SpO2Status.NORMAL) to
                "HTA + tahicardie: verifică anxietate, durere, tireotoxicoză; ajustează medicația antihipertensivă și eventual beta-blocante."
    )

    fun getRecommendation(hr: HRStatus, bp: BPStatus, spo2: SpO2Status): String =
        map[Triple(hr, bp, spo2)]
            ?: "Recomandare indisponibilă pentru această combinație."
}

// Modelul de date pentru o măsurătoare
data class Measurement(
    val timestamp: Date = Date(),
    val heartRate: Int,
    val systolic: Int,
    val diastolic: Int,
    val spo2: Int,
    val hrStatus: HRStatus = classifyHR(heartRate),
    val bpStatus: BPStatus = classifyBP(systolic, diastolic),
    val spo2Status: SpO2Status = classifySpO2(spo2),
    val recommendation: String = RecommendationMap.getRecommendation(hrStatus, bpStatus, spo2Status)
)

// Funcție de simulare a unei măsurători
fun simulateMeasurement(): Measurement {
    val hrStatus = HRStatus.values().random()
    val heartRate = when (hrStatus) {
        HRStatus.LOW    -> Random.nextInt(30, 60)
        HRStatus.NORMAL -> Random.nextInt(60, 101)
        HRStatus.HIGH   -> Random.nextInt(101, 140)
    }
    val bpStatus = BPStatus.values().random()
    val (systolic, diastolic) = when (bpStatus) {
        BPStatus.LOW    -> Pair(Random.nextInt(70, 90), Random.nextInt(40, 60))
        BPStatus.NORMAL -> Pair(Random.nextInt(90, 121), Random.nextInt(60, 81))
        BPStatus.HIGH   -> Pair(Random.nextInt(121, 161), Random.nextInt(81, 101))
    }
    val spo2Status = SpO2Status.values().random()
    val spo2 = when (spo2Status) {
        SpO2Status.LOW    -> Random.nextInt(80, 95)
        SpO2Status.NORMAL -> Random.nextInt(95, 101)
    }
    return Measurement(
        heartRate = heartRate,
        systolic = systolic,
        diastolic = diastolic,
        spo2 = spo2
    )
}
