# Configurable Minimal Watch Face

An original resource-only Wear OS Watch Face Format (WFF) v2 face for API 34, plus a small on-watch calendar-progress complication provider. Version **1.0.0**; package **`dev.alanshum.configurableminimal`**.

> Screenshot status: no screenshot is tracked. The target OPWWE251/API-34 watch accepted and activated this public package, and the declarative runtime loaded its four slots. The OEM picker initially showed **“Starting…”**, then loaded its management grid after a scoped System UI restart. This OPlus build exposes editors only for its own watch-face packages, so the public face's editor controls cannot be manually exercised there; its runtime style schema is nevertheless loaded and recorded by the declarative runtime. The OEM UI also does not expose the face surface to `screencap`. No private, third-party, picker, or unrelated launcher capture is substituted.

The face centers a two-line Orbitron MEDIUM clock, leaves clear wide text regions above and below, and uses small circular left/right complications. It contains no phone bridge, foreground service, exact alarm, notification sentinel, or combined-battery behavior.

<!-- CONFIG-INVENTORY:START -->
### Generated configuration inventory

**Flavors:** `daily` (Daily), `focus` (Focus), `signal` (Signal), `quiet` (Quiet)

| Configuration | Label | Default | Options |
| --- | --- | --- | --- |
| `clockColor` | Clock color | `aurora` | `aurora`, `ice`, `coral`, `violet`, `lime`, `amber`, `rose`, `mist` |
| `accentColor` | Accent color | `aurora` | `aurora`, `ice`, `coral`, `violet`, `lime`, `amber`, `rose`, `mist` |
| `aodStyle` | Always-on style | `dimmed` | `dimmed`, `timeOnly`, `large` |
| `seconds` | Seconds indicator | `FALSE` | `TRUE`, `FALSE` |
| `topTextSize` | Top text size | `22` | `18`, `22`, `26` |
| `bottomTextSize` | Bottom text size | `22` | `18`, `22`, `26` |

| Slot | Name | Types |
| --- | --- | --- |
| 0 | Day and date | `SHORT_TEXT EMPTY` |
| 1 | Calendar progress | `RANGED_VALUE SHORT_TEXT LONG_TEXT EMPTY` |
| 2 | Top text | `SHORT_TEXT LONG_TEXT EMPTY` |
| 3 | Bottom text | `SHORT_TEXT LONG_TEXT EMPTY` |
<!-- CONFIG-INVENTORY:END -->

## Configuration

There are eight independently selectable clock and accent colors; four original flavor presets; an optional seconds indicator; and always-on choices **Dimmed**, **Time only**, and **Enlarged time**. Top and bottom text have separate **18**, **22** (default), and **26** controls. Every listed setting has a rendering branch; the inventory generator derives the exact defaults, options, presets, and slots from WFF.

* **Left:** system Day & Date, `SHORT_TEXT`, defaults to two centered text/title lines with no provider icon.
* **Right:** the included Calendar progress ring is the default: `RANGED_VALUE` first, then `SHORT_TEXT`/`LONG_TEXT` and empty. A neutral full track covers basic/no-data states. The accented 0–12-hour arc decreases with remaining time; its minute-cadenced hand is hidden by an AMBIENT alpha variant.
* **Top / bottom:** `SHORT_TEXT`, `LONG_TEXT`, or empty. Top uses visual `TextCircular` inside a conservative rectangular editor target; bottom is straight centered text. Both safely render text plus title and expose independent sizes.

## Calendar progress provider

Install the `calendar-provider` APK, choose **Calendar progress ring** for the right slot, then open its setup entry and approve calendar permission. It queries `CalendarContract.Instances` only on the watch.

Selection is deterministic: a current event wins; overlapping current events select earliest end then stable event ID; otherwise the nearest future start then stable ID. All-day, cancelled, and declined entries are excluded. A current event reports remaining time; a future event reports time until start. Both use a 0–12-hour range and values above 12 hours display as a full ring. No qualifying event or missing permission returns no-data so the face retains a neutral fallback ring. The text/title convention is `NOW` / `NEXT`, rather than an unreliable visual dimming distinction.

The platform treats provider update periods as advisory. This provider asks for at-most-minute requests and bounds normal data to a minute/event boundary; it does not schedule alarms or keep a service alive, so updates can be delayed by system scheduling or ambient mode.

## Suggested layout (third-party apps are not bundled)

* Top: optional **Phone Battery Complication Event Timer** as a text provider if it provides the desired event/time-until/remaining text.
* Bottom: optional **Calendar Pro** `LONG_TEXT` next-event output as a text provider.
* Left: system **Day & Date**.
* Right: included **Calendar progress ring**.

These are optional user-configurable text choices, not bundled defaults. The owned watch-only provider is required for the numeric right ring; no phone companion is needed. Calendar Pro start/end-time formatting is provider/version dependent.

## Build and verify

```sh
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export ANDROID_HOME=/Users/alanshum/Library/Android/sdk
python3 tools/config_inventory.py --check
python3 -m unittest tools/test_config_inventory.py
python3 tools/wff_sanity_check.py
./gradlew test lint assembleDebug
git config core.hooksPath .githooks  # local repository only
```

`OFL.txt` and [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md) document the bundled official unmodified Orbitron font and its SHA-256.

`wff_sanity_check.py` provides deterministic structural/reference validation in CI alongside XML parsing and Android's resource build. It is intentionally a supplement rather than a substitute for the official Wear runtime's WFF v2 schema validation.
