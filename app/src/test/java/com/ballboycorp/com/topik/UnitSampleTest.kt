package com.ballboycorp.com.topik

import org.junit.Assert.assertEquals
import org.junit.Test

class UnitSampleTest {
    @Test
    fun someTest() {
        val result = UnitSample()
        assertEquals(result.getRanking(10), false)
    }

    @Test
    fun someTestTwo() {
        val result = UnitSample()
        assertEquals(result.getRanking(50), true)
    }
}