#!/usr/bin/env python3
"""Deterministic structural checks for the authored WFF v2 resource.

This is deliberately a complement to Android resource compilation: it checks the
cross references that the resource compiler cannot resolve.  The Android/Wear
runtime remains the schema authority.
"""
from pathlib import Path
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
WFF = ROOT / "watchface/src/main/res/raw/watchface.xml"
INFO = ROOT / "watchface/src/main/res/xml/watch_face_info.xml"


def fail(message):
    raise SystemExit(f"WFF sanity check: {message}")


def main():
    face = ET.parse(WFF).getroot()
    info = ET.parse(INFO).getroot()
    if face.tag != "WatchFace" or info.tag != "WatchFaceInfo":
        fail("expected WatchFace and WatchFaceInfo roots")
    required_info = {"Preview", "MultipleInstancesAllowed", "Editable", "FlavorsSupported"}
    if {node.tag for node in info} != required_info:
        fail("watch_face_info.xml must contain only the required v2 metadata")
    user_configurations = face.find("UserConfigurations")
    configurations = {
        node.attrib["id"]: ({option.attrib["id"] for option in node if option.tag.endswith("Option")}
                            or {"TRUE", "FALSE"})
        for node in user_configurations
        if node.tag in {"ColorConfiguration", "ListConfiguration", "BooleanConfiguration"}
    }
    required = {"accentColor", "clockColor", "aodStyle", "seconds", "topTextSize", "bottomTextSize"}
    if set(configurations) != required:
        fail(f"expected configuration IDs {sorted(required)}")
    for node in user_configurations.findall(".//Configuration"):
        if node.attrib["id"] not in configurations or node.attrib["optionId"] not in configurations[node.attrib["id"]]:
            fail(f"invalid flavor assignment {node.attrib}")
    slots = face.findall(".//ComplicationSlot")
    if [slot.attrib["slotId"] for slot in slots] != ["0", "1", "2", "3"]:
        fail("expected four stable complication slot IDs")
    if face.findall(".//BoundingArc"):
        fail("editor bounds must not use BoundingArc")
    if not face.findall(".//TextCircular"):
        fail("top complication must retain circular text rendering")


if __name__ == "__main__":
    main()
