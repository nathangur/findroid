package dev.jdtech.jellyfin.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import dev.jdtech.jellyfin.core.R as CoreR

class SettingsDownloadsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(CoreR.xml.fragment_settings_downloads, rootKey)
        
        findPreference<ListPreference>("pref_download_quality")?.setOnPreferenceChangeListener { preference, newValue ->
            true
        }

        findPreference<SeekBarPreference>("pref_storage_size_limit")?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                val sizeLimit = (newValue as Int) / 10.0 // Convert back to decimal
                val summary = when {
                    sizeLimit < 2.0 -> "2.0 GB"
                    newValue == this.max -> "Any" // Adjusted condition for "Any"
                    else -> "$sizeLimit GB"
                }
                preference.summary = summary
                true
            }
            // Initial summary setup
            val initialSizeLimit = sharedPreferences?.getInt("pref_storage_size_limit", 100)
                ?.div(10.0)
            val initialSummary = when {
                initialSizeLimit!! < 2.0 -> "2.0 GB"
                initialSizeLimit.toInt() == 40 -> "Any" // Corrected to check against 40 instead of max / 10.0
                else -> "$initialSizeLimit GB"
            }
            summary = initialSummary
        }

        findPreference<SwitchPreferenceCompat>("pref_download_original")?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                val isDownloadOriginalEnabled = newValue as Boolean
                Toast.makeText(
                    requireContext(), 
                    getString(if (isDownloadOriginalEnabled) CoreR.string.download_original_enabled else CoreR.string.download_original_disabled), 
                    Toast.LENGTH_LONG
                ).show()
                true
            }
        }
    }
}
