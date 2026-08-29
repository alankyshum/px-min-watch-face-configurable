package dev.alanshum.configurableminimal.calendar

/** Pure selection policy so calendar data handling remains deterministic and testable. */
data class CalendarEvent(val id: Long, val begin: Long, val end: Long, val title: String)
data class SelectedEvent(val event: CalendarEvent, val current: Boolean)

object EventSelector {
    fun select(now: Long, events: List<CalendarEvent>): SelectedEvent? {
        val current = events.filter { it.begin <= now && now < it.end }
            .minWithOrNull(compareBy<CalendarEvent> { it.end }.thenBy { it.id })
        if (current != null) return SelectedEvent(current, true)
        val next = events.filter { it.begin > now }
            .minWithOrNull(compareBy<CalendarEvent> { it.begin }.thenBy { it.id })
        return next?.let { SelectedEvent(it, false) }
    }
    fun hoursUntil(now: Long, selected: SelectedEvent): Float =
        (((if (selected.current) selected.event.end else selected.event.begin) - now) / 3_600_000f)
            .coerceIn(0f, 12f)
}
