package com.example.timewallet.record

/**
 * Datenklasse, die eine Arbeitsaufzeichnung repräsentiert.
 *
 * Jede Instanz dieser Klasse speichert Informationen über eine einzelne Arbeitsaufzeichnung,
 * einschließlich des Datums, der Start- und Endzeit, der geleisteten Arbeitsstunden und der Chip-Eingabe.
 *
 * @property date Das Datum der Arbeitsaufzeichnung.
 * @property startTime Die Startzeit der Arbeitsperiode.
 * @property endTime Die Endzeit der Arbeitsperiode.
 * @property workedHours Die geleisteten Arbeitsstunden in Stunden.
 * @property chipInput Eine Eingabe des Chips, die zusätzliche Informationen zu der Arbeitsperiode enthalten kann.
 *
 * @author Marco Martins
 * @created 07.11.2023
 */
data class WorkRecord(
    val date: String,
    val startTime: String,
    val endTime: String,
    val workedHours: String,
    val chipInput: String
)
