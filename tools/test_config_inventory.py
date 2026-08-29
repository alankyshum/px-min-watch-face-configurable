import subprocess
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


class InventoryTest(unittest.TestCase):
    def test_current_inventory_is_fresh(self):
        subprocess.run(["python3", "tools/config_inventory.py", "--check"], cwd=ROOT, check=True)

    def test_stale_marker_is_rejected(self):
        readme = ROOT / "README.md"
        original = readme.read_text()
        try:
            readme.write_text(original.replace("`daily`", "`outdated`", 1))
            result = subprocess.run(["python3", "tools/config_inventory.py", "--check"], cwd=ROOT)
            self.assertNotEqual(result.returncode, 0)
        finally:
            readme.write_text(original)


if __name__ == "__main__":
    unittest.main()
