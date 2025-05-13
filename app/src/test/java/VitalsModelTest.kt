package com.example.smartbandsimulator.vitals

import org.junit.Assert.*
import org.junit.Test

class VitalsModelTest {
    @Test fun `classifyHR returns LOW for hr < 60`() =
        assertEquals(HRStatus.LOW, classifyHR(45))

    @Test fun `classifyHR returns NORMAL for 60-100`() =
        assertEquals(HRStatus.NORMAL, classifyHR(80))

    @Test fun `classifyHR returns HIGH for >100`() =
        assertEquals(HRStatus.HIGH, classifyHR(120))

    @Test fun `classifyBP returns LOW for <90/60`() =
        assertEquals(BPStatus.LOW, classifyBP(85, 55))

    @Test fun `classifyBP returns NORMAL for 90-120/60-80`() =
        assertEquals(BPStatus.NORMAL, classifyBP(115, 75))

    @Test fun `classifyBP returns HIGH for >120/80`() =
        assertEquals(BPStatus.HIGH, classifyBP(130, 85))

    @Test fun `classifySpO2 returns LOW for <95`() =
        assertEquals(SpO2Status.LOW, classifySpO2(90))

    @Test fun `classifySpO2 returns NORMAL for >=95`() =
        assertEquals(SpO2Status.NORMAL, classifySpO2(98))

    @Test fun `getRecommendation for HIGH/HIGH/NORMAL`() {
        val rec = RecommendationMap.getRecommendation(HRStatus.HIGH, BPStatus.HIGH, SpO2Status.NORMAL)
        assertTrue(rec.startsWith("HTA + tahicardie"))
    }

    @Test fun `simulateMeasurement produces values in correct ranges`() {
        repeat(5) {
            val m = simulateMeasurement()
            when (m.hrStatus) {
                HRStatus.LOW    -> assertTrue(m.heartRate in 30..59)
                HRStatus.NORMAL -> assertTrue(m.heartRate in 60..100)
                HRStatus.HIGH   -> assertTrue(m.heartRate in 101..139)
            }
            when (m.bpStatus) {
                BPStatus.LOW    -> {
                    assertTrue(m.systolic in 70..89)
                    assertTrue(m.diastolic in 40..59)
                }
                BPStatus.NORMAL -> {
                    assertTrue(m.systolic in 90..120)
                    assertTrue(m.diastolic in 60..80)
                }
                BPStatus.HIGH   -> {
                    assertTrue(m.systolic in 121..160)
                    assertTrue(m.diastolic in 81..100)
                }
            }
            when (m.spo2Status) {
                SpO2Status.LOW    -> assertTrue(m.spo2 in 80..94)
                SpO2Status.NORMAL -> assertTrue(m.spo2 in 95..100)
            }
        }
    }
}
