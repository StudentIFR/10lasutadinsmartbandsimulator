package com.example.smartbandsimulator.vitals

// 1. Enum-uri de status pentru fiecare parametru
enum class HRStatus { LOW, NORMAL, HIGH }
enum class StepsStatus { SEDENTARY, ACTIVE, VERY_ACTIVE }
enum class BPStatus { LOW, NORMAL, PREHYPERTENSION, HYPERTENSION }
enum class CalorieStatus { LOW, NORMAL, HIGH }
enum class SleepStatus { INSUFFICIENT, ADEQUATE, EXCESSIVE }
enum class TempStatus { NORMAL, FEVER }

// 2. Funcții de clasificare a valorilor

/**
 * Clasifică ritmul cardiac (bpm):
 * - sub 60: LOW
 * - între 60 și 100: NORMAL
 * - peste 100: HIGH
 */
fun classifyHR(hr: Int): HRStatus = when {
    hr < 60   -> HRStatus.LOW
    hr <= 100 -> HRStatus.NORMAL
    else      -> HRStatus.HIGH
}

/**
 * Clasifică numărul de pași:
 * - sub 5000: SEDENTARY
 * - între 5000 și 9999: ACTIVE
 * - 10000 sau mai mulți: VERY_ACTIVE
 */
fun classifySteps(steps: Int): StepsStatus = when {
    steps < 5000    -> StepsStatus.SEDENTARY
    steps < 10000   -> StepsStatus.ACTIVE
    else            -> StepsStatus.VERY_ACTIVE
}

/**
 * Clasifică tensiunea arterială:
 * - sistolic < 90 sau diastolic < 60: LOW
 * - sistolic <= 120 și diastolic <= 80: NORMAL
 * - sistolic <= 140 și diastolic <= 90: PREHYPERTENSION
 * - altfel: HYPERTENSION
 */
fun classifyBloodPressure(systolic: Int, diastolic: Int): BPStatus = when {
    systolic < 90 || diastolic < 60      -> BPStatus.LOW
    systolic <= 120 && diastolic <= 80   -> BPStatus.NORMAL
    systolic <= 140 && diastolic <= 90   -> BPStatus.PREHYPERTENSION
    else                                 -> BPStatus.HYPERTENSION
}

/**
 * Clasifică caloriile consumate:
 * - sub 100: LOW
 * - între 100 și 300: NORMAL
 * - peste 300: HIGH
 */
fun classifyCalories(cal: Int): CalorieStatus = when {
    cal < 100    -> CalorieStatus.LOW
    cal <= 300   -> CalorieStatus.NORMAL
    else         -> CalorieStatus.HIGH
}

/**
 * Clasifică durata somnului (ore):
 * - sub 6: INSUFFICIENT
 * - între 6 și 9: ADEQUATE
 * - peste 9: EXCESSIVE
 */
fun classifySleep(hours: Double): SleepStatus = when {
    hours < 6.0   -> SleepStatus.INSUFFICIENT
    hours <= 9.0  -> SleepStatus.ADEQUATE
    else          -> SleepStatus.EXCESSIVE
}

/**
 * Clasifică temperatura corpului (°C):
 * - 38.0 sau mai mare: FEVER
 * - altfel: NORMAL
 */
fun classifyTemperature(temp: Double): TempStatus =
    if (temp >= 38.0) TempStatus.FEVER else TempStatus.NORMAL
