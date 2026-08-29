package dev.alanshum.configurableminimal.calendar

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class SetupActivity : Activity() {
    override fun onCreate(state: Bundle?) { super.onCreate(state)
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(24, 24, 24, 24) }
        layout.addView(TextView(this).apply { text = getString(dev.alanshum.configurableminimal.calendar.R.string.setup_body) })
        layout.addView(Button(this).apply { text = getString(R.string.grant); setOnClickListener { requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR), 8) } })
        setContentView(layout); setResult(RESULT_OK)
    }
}
