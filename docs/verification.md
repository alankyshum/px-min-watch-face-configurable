# Public-face verification record

## Active-face observation

The public `dev.alanshum.configurableminimal` package was observed active on OPWWE251. The platform identified its declarative host as `com.google.wear.watchface.runtime/.DeclarativeWatchFaceRuntime0`, with the active instance recorded as `wfId-66`. The watch picker listed the public face and supports the on-watch path **watch face picker → Add → Configurable Minimal**.

The declarative runtime loaded the six WFF setting IDs `clockColor`, `accentColor`, `aodStyle`, `seconds`, `topTextSize`, and `bottomTextSize`, plus the four declared complication slots. OHealth's phone-side editor did not expose remote editing for this third-party standard WFF2 schema; editing is watch-side/platform-managed. This record deliberately excludes raw device output, serials, IP addresses, notification data, and calendar content.

## Screenshot record

`docs/images/watch-face-active.png` is a 466 × 466 RGBA PNG captured from the active watch-face surface. Its SHA-256 is:

```text
03a3d9423f4f70d3e9e5f485db764823c5300e340c3de2ebaf72090010fa4e9d
```

The screenshot has no PNG text chunks or EXIF metadata and was visually reviewed for private event titles, device IDs, IP addresses, notifications, and other personal information. None was present. It is unannotated and was not sanitized because no sensitive content was found.
