# Public-face verification record

## Hands-on observation

The public `dev.alanshum.configurableminimal` face was active as `wfId-66` in the WFF renderer 2.0.1 on OPWWE251 / API 34. The watch-side editor is reached by long-pressing the face and choosing **Customize**, or by **watch-face picker → Add → Configurable Minimal**. On the tested OnePlus/OHealth pairing, OHealth cannot remotely edit standard third-party WFF2 schemas; settings are edited through the watch's platform controls.

### Settings and flavors

All settings were changed on the watch, their visible behavior was observed, and their selections persisted after leaving and returning to the editor.

| Control | Exercised values | Observed behavior |
| --- | --- | --- |
| `clockColor` | Multiple palette choices | Changes the clock color. |
| `accentColor` | Multiple palette choices | Changes accent elements independently. |
| `aodStyle` | `dimmed`, `timeOnly`, `large` | Selects the ambient presentation below. |
| `seconds` | `true`, `false` | Changes the interactive seconds indicator; it is hidden in ambient. |
| `topTextSize` | `18`, `22`, `26` | Changes the top text size. |
| `bottomTextSize` | `18`, `22`, `26` | Changes the bottom text size. |

The `Daily`, `Focus`, `Signal`, and `Quiet` flavors were also exercised. The final restored state was `aurora` clock color, `aurora` accent color, `dimmed` AOD, seconds `false`, top text `22`, and bottom text `22`.

### Complication slots

| Slot / runtime ID | Verified content |
| --- | --- |
| 0 / 11 | System Day & Date, `SHORT_TEXT`, rendered as two lines with no icon. |
| 1 / 12 | Owned `dev.alanshum.configurableminimal.calendar/.CalendarProgressService`, `RANGED_VALUE`: neutral track plus a decreasing highlighted 0–12-hour arc; arrow is hidden in ambient. |
| 2 / 13 | Curved `SHORT_TEXT` / `LONG_TEXT`; sizes `18`, `22`, and `26` work. |
| 3 / 14 | Straight `SHORT_TEXT` / `LONG_TEXT`; sizes `18`, `22`, and `26` work. |

### Calendar provider semantics and cadence

Calendar permission was granted only to the owned provider. A current event emits `NOW` with hours remaining; a future event emits `NEXT` with time until it starts; no qualifying event emits `NoData`. The reported range is minimum `0`, maximum `12`; the `capIsTwelveHours` behavior was observed for a value beyond 12 hours. `validTimeRange`, bounded to the event or next minute boundary, confirms minute cadence without second polling.

### Always-on display matrix

| AOD style | Standard clock | Large clock | Slots |
| --- | ---: | ---: | ---: |
| `dimmed` | alpha 150 | — | alpha 130 |
| `timeOnly` | alpha 255 | — | alpha 0 |
| `large` | alpha 0 | alpha 255 | alpha 0 |

The arrow and seconds indicator have alpha 0 in every ambient mode.

## Evidence boundaries

`docs/images/watch-face-active.png` remains the privacy-safe, unannotated active-face capture: 466 × 466, SHA-256 `03a3d9423f4f70d3e9e5f485db764823c5300e340c3de2ebaf72090010fa4e9d`. It has no PNG text chunks or EXIF metadata and was visually checked for private event titles, device identifiers, IP addresses, notifications, and other personal information.

This public record intentionally omits raw device logs, serials, network addresses, calendar content, and any additional screenshots. It records hands-on results on the stated device/runtime; it does not claim behavior on other OEMs, OS releases, calendar providers, or companion apps.
