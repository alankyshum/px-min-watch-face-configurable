package dev.alanshum.configurableminimal.calendar
import org.junit.Assert.assertEquals
import org.junit.Test
class EventSelectorTest {
 @Test fun currentWinsAndEndsFirst() { val result = EventSelector.select(100, listOf(CalendarEvent(9, 50, 200, "a"), CalendarEvent(3, 80, 150, "b"), CalendarEvent(1, 110, 200, "c"))); assertEquals(3, result!!.event.id); assertEquals(true, result.current) }
 @Test fun nextIsBeginThenId() { val result = EventSelector.select(100, listOf(CalendarEvent(9, 120, 200, "a"), CalendarEvent(3, 120, 150, "b"))); assertEquals(3, result!!.event.id); assertEquals(false, result.current) }
 @Test fun capIsTwelveHours() { assertEquals(12f, EventSelector.hoursUntil(0, SelectedEvent(CalendarEvent(1, 13 * 3_600_000L, 14 * 3_600_000L, "x"), false))) }
}
