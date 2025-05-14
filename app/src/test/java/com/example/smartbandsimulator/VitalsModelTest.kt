package com.example.smartbandsimulator.vitals

import org.junit.Assert.assertEquals
import org.junit.Test

import com.example.smartbandsimulator.vitals.*

class VitalsModelTest {

    @Test
    fun `classifyHR returns LOW for hr less than 60`() {
        assertEquals(HRStatus.LOW, classifyHR(45))
    }

    @Test
    fun `classifyHR returns NORMAL for hr between 60 and 100 inclusive`() {
        assertEquals(HRStatus.NORMAL, classifyHR(60))
        assertEquals(HRStatus.NORMAL, classifyHR(80))
        assertEquals(HRStatus.NORMAL, classifyHR(100))
    }

    @Test
    fun `classifyHR returns HIGH for hr greater than 100`() {
        assertEquals(HRStatus.HIGH, classifyHR(120))
    }

    @Test
    fun `classifySteps returns SEDENTARY for steps less than 5000`() {
        assertEquals(StepsStatus.SEDENTARY, classifySteps(3000))
    }

    @Test
    fun `classifySteps returns ACTIVE for steps between 5000 and 9999`() {
        assertEquals(StepsStatus.ACTIVE, classifySteps(5000))
        assertEquals(StepsStatus.ACTIVE, classifySteps(9999))
    }

    @Test
    fun `classifySteps returns VERY ACTIVE for steps 10000 or more`() {
        assertEquals(StepsStatus.VERY_ACTIVE, classifySteps(10000))
    }

    @Test
    fun `classifyBloodPressure returns LOW when systolic less than 90 or diastolic less than 60`() {
        assertEquals(BPStatus.LOW, classifyBloodPressure(85, 75))
        assertEquals(BPStatus.LOW, classifyBloodPressure(95, 55))
    }

    @Test
    fun `classifyBloodPressure returns NORMAL when systolic up to 120 and diastolic up to 80`() {
        assertEquals(BPStatus.NORMAL, classifyBloodPressure(120, 80))
    }

    @Test
    fun `classifyBloodPressure returns PREHYPERTENSION when systolic up to 140 and diastolic up to 90`() {
        assertEquals(BPStatus.PREHYPERTENSION, classifyBloodPressure(140, 90))
    }

    @Test
    fun `classifyBloodPressure returns HYPERTENSION when systolic greater than 140 or diastolic greater than 90`() {
        assertEquals(BPStatus.HYPERTENSION, classifyBloodPressure(150, 95))
    }

    @Test
    fun `classifyCalories returns LOW for calories less than 100`() {
        assertEquals(CalorieStatus.LOW, classifyCalories(50))
    }

    @Test
    fun `classifyCalories returns NORMAL for calories between 100 and 300 inclusive`() {
        assertEquals(CalorieStatus.NORMAL, classifyCalories(100))
        assertEquals(CalorieStatus.NORMAL, classifyCalories(300))
    }

    @Test
    fun `classifyCalories returns HIGH for calories greater than 300`() {
        assertEquals(CalorieStatus.HIGH, classifyCalories(400))
    }

    @Test
    fun `classifySleep returns INSUFFICIENT for hours less than 6`() {
        assertEquals(SleepStatus.INSUFFICIENT, classifySleep(5.5))
    }

    @Test
    fun `classifySleep returns ADEQUATE for hours between 6 and 9 inclusive`() {
        assertEquals(SleepStatus.ADEQUATE, classifySleep(6.0))
        assertEquals(SleepStatus.ADEQUATE, classifySleep(9.0))
    }

    @Test
    fun `classifySleep returns EXCESSIVE for hours greater than 9`() {
        assertEquals(SleepStatus.EXCESSIVE, classifySleep(10.0))
    }

    @Test
    fun `classifyTemperature returns NORMAL for temperature below 38`() {
        assertEquals(TempStatus.NORMAL, classifyTemperature(37.5))
    }

    @Test
    fun `classifyTemperature returns FEVER for temperature 38 or above`() {
        assertEquals(TempStatus.FEVER, classifyTemperature(38.0))
    }
}
