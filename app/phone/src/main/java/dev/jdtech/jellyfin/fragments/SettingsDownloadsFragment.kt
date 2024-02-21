package dev.jdtech.jellyfin.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import dev.jdtech.jellyfin.core.R as CoreR

class SettingsDownloadsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(CoreR.xml.fragment_settings_downloads, rootKey)
        
        findPreference<ListPreference>("pref_download_quality")?.setOnPreferenceChangeListener { preference, newValue ->
            AlertDialog.Builder(requireContext())
                .setTitle("Download Quality Changed")
                .setMessage("New download quality selected: $newValue")
                .setPositiveButton("OK", null)
                .show()
            true
        }

        findPreference<SeekBarPreference>("pref_download_size_limit")?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                val sizeLimit = (newValue as Int) / 10.0 // Convert back to decimal
                val summary = if (sizeLimit < 2.0) "2.0 GB" else if (sizeLimit == this.max / 10.0) "Any" else "$sizeLimit GB"
                preference.summary = summary
                true
            }
            // Initial summary setup
            val initialSizeLimit = sharedPreferences?.getInt("pref_download_size_limit", 100)
                ?.div(10.0)
            val initialSummary = if (initialSizeLimit!! < 2.0) "2.0 GB" else if (initialSizeLimit == this.max / 10.0) "No Limit" else "$initialSizeLimit GB"
            summary = initialSummary
        }
    }
}
