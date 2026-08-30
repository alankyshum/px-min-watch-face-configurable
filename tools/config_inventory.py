#!/usr/bin/env python3
"""Keep the README configuration inventory derived from the authored WFF XML."""
from pathlib import Path
import argparse
import re
import xml.etree.ElementTree as ET
ROOT = Path(__file__).resolve().parents[1]
XML = ROOT / "watchface/src/main/res/raw/watchface.xml"
README = ROOT / "README.md"
START, END = "<!-- CONFIG-INVENTORY:START -->", "<!-- CONFIG-INVENTORY:END -->"
def inventory():
    text = XML.read_text()
    root = ET.fromstring(text)
    flavors = re.findall(r'<Flavor id="([^"]+)" displayName="([^"]+)"', text)
    slots = re.findall(r'<ComplicationSlot [^>]*slotId="([^"]+)" displayName="([^"]+)" supportedTypes="([^"]+)"', text)
    configurations = []
    for configuration in root.find("UserConfigurations"):
        if configuration.tag in {"ColorConfiguration", "ListConfiguration", "BooleanConfiguration"}:
            options = [option.attrib["id"] for option in configuration if option.tag.endswith("Option")]
            configurations.append((configuration.attrib["id"], configuration.attrib["displayName"], configuration.attrib["defaultValue"], options))
    rows = [f"| `{identifier}` | {label} | `{default}` | {', '.join(f'`{option}`' for option in options) or '`TRUE`, `FALSE`'} |" for identifier, label, default, options in configurations]
    return "\n".join([START, "### Generated configuration inventory", "", "**Flavors:** " + ", ".join(f"`{name}` ({label})" for name, label in flavors), "", "| Configuration | Label | Default | Options |", "| --- | --- | --- | --- |", *rows, "", "| Slot | Name | Types |", "| --- | --- | --- |", *[f"| {sid} | {name} | `{types}` |" for sid, name, types in slots], END])
def main():
    p=argparse.ArgumentParser(); p.add_argument("--check", action="store_true"); a=p.parse_args(); body=inventory(); old=README.read_text()
    new=re.sub(re.escape(START)+r".*?"+re.escape(END), body, old, flags=re.S)
    if a.check: raise SystemExit("README inventory is stale; run tools/config_inventory.py" if old != new else 0)
    README.write_text(new)
if __name__ == "__main__": main()
