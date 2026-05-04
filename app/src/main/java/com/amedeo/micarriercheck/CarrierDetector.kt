package com.amedeo.micarriercheck

import java.io.BufferedReader
import java.io.InputStreamReader

/** The four system properties this app inspects. */
val TRACKED_PROPERTIES = listOf(
    "ro.miui.carrier.cota",
    "persist.sys.cota.carrier",
    "persist.sys.carrier.name",
    "persist.sys.lockzone.channel",
)

/**
 * Result of a carrier detection pass.
 *
 * @param isCarrierLocked  True when `ro.miui.carrier.cota` == "true".
 * @param carrier          Matched [Carrier] from the registry, or [XIAOMI_DEFAULT].
 * @param rawProperties    Raw key→value map for the details panel.
 */
data class DetectionResult(
    val isCarrierLocked: Boolean,
    val carrier: Carrier,
    val rawProperties: Map<String, String>,
)

/** Reads all [TRACKED_PROPERTIES] and returns a [DetectionResult]. */
fun detectCarrier(): DetectionResult {
    val props = TRACKED_PROPERTIES.associateWith { readProperty(it) }

    val isLocked = props["ro.miui.carrier.cota"]?.lowercase() == "true"

    // Resolve the raw code: prefer carrier.name → cota.carrier → lockzone.channel
    val rawCode = props["persist.sys.cota.carrier"]?.takeIf { it.isNotBlank() }
        ?: props["persist.sys.carrier.name"]?.takeIf { it.isNotBlank() }
        ?: props["persist.sys.lockzone.channel"]?.takeIf { it.isNotBlank() }
        ?: ""

    val carrier = resolveCarrier(rawCode)

    return DetectionResult(
        isCarrierLocked = isLocked,
        carrier = carrier,
        rawProperties = props,
    )
}

/**
 * Matches a raw property value against [CARRIER_REGISTRY] by checking whether
 * any known 2-letter code appears in the string (e.g. "DT-262" matches "DT").
 * Falls back to [XIAOMI_DEFAULT] when no match is found.
 */
private fun resolveCarrier(rawCode: String): Carrier {
    if (rawCode.isBlank()) return XIAOMI_DEFAULT
    val matchedCode = CARRIER_REGISTRY.keys
        .firstOrNull { rawCode.contains(it, ignoreCase = true) }
    return CARRIER_REGISTRY[matchedCode] ?: XIAOMI_DEFAULT
}

/** Executes `getprop <key>` and returns the trimmed output, or "" on failure. */
private fun readProperty(key: String): String {
    return try {
        val process = Runtime.getRuntime().exec(arrayOf("getprop", key))
        val value = BufferedReader(InputStreamReader(process.inputStream))
            .use { it.readLine()?.trim() }
        process.destroy()
        value.orEmpty()
    } catch (_: Exception) {
        ""
    }
}
