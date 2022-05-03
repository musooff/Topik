package com.ballboycorp.com.topik

import org.junit.Assert.*

import org.junit.Test

class AndroidSampleTest {

    @Test
    fun isContextNonNull() {
        val androidSample = AndroidSample(null)
        assertEquals(androidSample.isContextNonNull(), false)
    }
}