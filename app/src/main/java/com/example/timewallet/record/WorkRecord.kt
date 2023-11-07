package com.example.timewallet.record

data class WorkRecord(
    val date: String,
    val startTime: String,
    val endTime: String,
    val workedHours: Int,
    val chipInput: String
)
