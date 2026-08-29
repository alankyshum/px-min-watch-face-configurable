package dev.alanshum.configurableminimal.calendar

import android.Manifest
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import java.time.Instant

class CalendarProgressService : SuspendingComplicationDataSourceService() {
    override fun getPreviewData(type: androidx.wear.watchface.complications.data.ComplicationType): ComplicationData? = when (type) {
        androidx.wear.watchface.complications.data.ComplicationType.RANGED_VALUE -> RangedValueComplicationData.Builder(6f, 0f, 12f, PlainComplicationText.Builder("6h left").build()).setTitle(PlainComplicationText.Builder("NOW").build()).build()
        androidx.wear.watchface.complications.data.ComplicationType.LONG_TEXT -> LongTextComplicationData.Builder(PlainComplicationText.Builder("NEXT: in 2h · Sample event").build(), PlainComplicationText.Builder("NEXT").build()).build()
        androidx.wear.watchface.complications.data.ComplicationType.SHORT_TEXT -> ShortTextComplicationData.Builder(PlainComplicationText.Builder("in 2h").build(), PlainComplicationText.Builder("NEXT").build()).build()
        else -> null
    }
    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) return NoDataComplicationData()
        val now = System.currentTimeMillis()
        val events = mutableListOf<CalendarEvent>()
        contentResolver.query(CalendarContract.Instances.CONTENT_URI.buildUpon().appendPath((now - 12 * HOUR).toString()).appendPath((now + 12 * HOUR).toString()).build(),
            arrayOf(CalendarContract.Instances.EVENT_ID, CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.TITLE, CalendarContract.Instances.ALL_DAY, CalendarContract.Instances.STATUS, CalendarContract.Instances.SELF_ATTENDEE_STATUS),
            "${CalendarContract.Instances.ALL_DAY}=0 AND ${CalendarContract.Instances.STATUS}!=? AND ${CalendarContract.Instances.SELF_ATTENDEE_STATUS}!=?",
            arrayOf(CalendarContract.Events.STATUS_CANCELED.toString(), CalendarContract.Attendees.ATTENDEE_STATUS_DECLINED.toString()), null)?.use { cursor ->
            while (cursor.moveToNext()) events += CalendarEvent(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2), cursor.getString(3) ?: "Event")
        }
        val selected = EventSelector.select(now, events) ?: return NoDataComplicationData()
        val hours = EventSelector.hoursUntil(now, selected)
        val text = if (selected.current) "${hours.toInt()}h left" else "in ${hours.toInt()}h"
        val title = if (selected.current) "NOW" else "NEXT"
        // Request cadence is advisory; the valid range bounds a displayed value to the next minute
        // or event boundary without an alarm, service, or second-based update loop.
        val boundary = if (selected.current) selected.event.end else selected.event.begin
        val nextChange = Instant.ofEpochMilli(minOf(boundary, now + 60_000L).coerceAtLeast(now + 1L))
        return when (request.complicationType) {
            androidx.wear.watchface.complications.data.ComplicationType.RANGED_VALUE -> RangedValueComplicationData.Builder(hours, 0f, 12f, PlainComplicationText.Builder(text).build()).setTitle(PlainComplicationText.Builder(title).build()).setTapAction(null).setValidTimeRange(androidx.wear.watchface.complications.data.TimeRange.between(Instant.ofEpochMilli(now), nextChange)).build()
            androidx.wear.watchface.complications.data.ComplicationType.LONG_TEXT -> LongTextComplicationData.Builder(PlainComplicationText.Builder("$title: $text · ${selected.event.title}").build(), PlainComplicationText.Builder(title).build()).build()
            else -> ShortTextComplicationData.Builder(PlainComplicationText.Builder(text).build(), PlainComplicationText.Builder(title).build()).build()
        }
    }
    private companion object { const val HOUR = 3_600_000L }
}
