# Configurable Minimal Watch Face

An original resource-only Wear OS Watch Face Format (WFF) v2 face for API 34, plus a small on-watch calendar-progress complication provider. Version **1.0.0**; package **`dev.alanshum.configurableminimal`**.

> Screenshot status: no screenshot is tracked yet. The clean-room APK was installed on the target API-34 watch, but its OEM picker remained at “Starting…” rather than presenting a selectable face. No private or third-party screenshot is substituted. This is documented as a deployment verification blocker below.

The face centers a two-line Orbitron MEDIUM clock, leaves clear wide text regions above and below, and uses small circular left/right complications. It contains no phone bridge, foreground service, exact alarm, notification sentinel, or combined-battery behavior.

<!-- CONFIG-INVENTORY:START -->
### Generated configuration inventory

**Flavors:** `daily` (Daily), `focus` (Focus), `signal` (Signal), `quiet` (Quiet)

| Slot | Name | Types |
| --- | --- | --- |
| 0 | Day and date | `SHORT_TEXT EMPTY` |
| 1 | Calendar progress | `RANGED_VALUE SHORT_TEXT LONG_TEXT EMPTY` |
| 2 | Top text | `SHORT_TEXT LONG_TEXT EMPTY` |
| 3 | Bottom text | `SHORT_TEXT LONG_TEXT EMPTY` |
<!-- CONFIG-INVENTORY:END -->

## Configuration

There are eight independently selectable clock and accent colors; four original flavor presets; an optional seconds indicator; and always-on choices **Dimmed**, **Time only**, and **Enlarged time**. The top/bottom size control exposes **18**, **22** (default), and **26**. The current WFF layout uses 22 as its baseline wide-text rendering; the inventory generator ensures the options and presets remain documented.

* **Left circle:** system Day & Date, `SHORT_TEXT`, defaulted to text-only and centered. It deliberately does not render a complication icon.
* **Right circle:** `RANGED_VALUE` first, then `SHORT_TEXT`/`LONG_TEXT`. A neutral full track is used for no data/basic types. For a ranged value the accented arc maps 0–12 hours and shrinks toward zero. Its arrow/hand is hidden by an AMBIENT alpha variant.
* **Top wide/curved and bottom wide:** `SHORT_TEXT`, `LONG_TEXT`, or empty, rendered as text plus title. The 360-pixel top width has room for a compact `xx:xx–yy:yy TITLE` provider value at the default size; providers ultimately control their own text length.

## Calendar progress provider

Install the `calendar-provider` APK, choose **Calendar progress ring** for the right slot, then open its setup entry and approve calendar permission. It queries `CalendarContract.Instances` only on the watch.

Selection is deterministic: a current event wins; overlapping current events select earliest end then stable event ID; otherwise the nearest future start then stable ID. All-day, cancelled, and declined entries are excluded. A current event reports remaining time; a future event reports time until start. Both use a 0–12-hour range and values above 12 hours display as a full ring. No qualifying event or missing permission returns no-data so the face retains a neutral fallback ring. The text/title convention is `NOW` / `NEXT`, rather than an unreliable visual dimming distinction.

The platform treats provider update periods as advisory. This provider asks for at-most-minute requests and bounds normal data to a minute/event boundary; it does not schedule alarms or keep a service alive, so updates can be delayed by system scheduling or ambient mode.

## Suggested layout (third-party apps are not bundled)

* Top: optional **Phone Battery Complication Event Timer** if it provides the desired event/time-until/remaining text.
* Bottom: optional **Calendar Pro** `LONG_TEXT` next-event output.
* Left: system **Day & Date**.
* Right: included **Calendar progress ring**.

These are user-configurable complication choices. Calendar Pro start/end-time formatting is not promised because it is provider/version dependent.

## Build and verify

```sh
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export ANDROID_HOME=/Users/alanshum/Library/Android/sdk
python3 tools/config_inventory.py --check
python3 -m unittest tools/test_config_inventory.py
./gradlew test lint assembleDebug
git config core.hooksPath .githooks  # local repository only
```

`OFL.txt` and [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md) document the bundled official unmodified Orbitron font and its SHA-256.
