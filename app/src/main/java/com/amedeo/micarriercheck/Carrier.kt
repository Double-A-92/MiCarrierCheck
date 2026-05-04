package com.amedeo.micarriercheck

import androidx.compose.ui.graphics.Color

/**
 * Represents a known Xiaomi carrier customization.
 *
 * @param displayName   Human-readable carrier name shown in the UI.
 * @param code          Two-letter Xiaomi internal carrier code (e.g. "DT").
 * @param brandColor    Primary brand color used for theming and badge border.
 * @param logoAssetPath Path within the assets folder, e.g. "logos/logo_dt.svg".
 *                      Null means no logo asset is available; a letter badge is used as fallback.
 */
data class Carrier(
    val displayName: String,
    val code: String,
    val brandColor: Color,
    val logoAssetPath: String? = null,
)

/**
 * Xiaomi default — shown when COTA lock is not active.
 */
val XIAOMI_DEFAULT = Carrier(
    displayName = "Xiaomi",
    code = "XM",
    brandColor = Color(0xFFFD6800),
    logoAssetPath = "logos/logo_xiaomi.svg",
)

/**
 * Complete registry of known Xiaomi carrier codes.
 * Keyed by the two-letter carrier code that appears in the system properties.
 *
 * To add a new carrier: add one entry here and drop the logo file in assets/logos/.
 */
val CARRIER_REGISTRY: Map<String, Carrier> = listOf(
    Carrier("MiStore (Demo)",       "DM", Color(0xFFFD6800)),
    Carrier("DeviceLockController", "DC", Color(0xFF12B4CA)),
    Carrier("AT&T",                 "AT", Color(0xFF00A7DE), "logos/logo_att.svg"),
    Carrier("Bouygues",             "BY", Color(0xFF009CCB), "logos/logo_bouygues.svg"),
    Carrier("Claro",                "CR", Color(0xFFE2242D), "logos/logo_claro.svg"),
    Carrier("Entel",                "EN", Color(0xFFF37817), "logos/logo_entel.svg"),
    Carrier("3HK",                  "HG", Color(0xFF000000), "logos/logo_3hk.svg"),
    Carrier("KDDI",                 "KD", Color(0xFF2B449D), "logos/logo_kddi.svg"),
    Carrier("Movistar",             "MS", Color(0xFF7FB927), "logos/logo_movistar.svg"),
    Carrier("MTN",                  "MT", Color(0xFFFCCA00), "logos/logo_mtn.svg"),
    Carrier("Orange",               "OR", Color(0xFFFD7800), "logos/logo_orange.svg"),
    Carrier("SoftBank",             "SB", Color(0xFF535051), "logos/logo_softbank.svg"),
    Carrier("Altice France (SFR)",  "SF", Color(0xFFEA0004), "logos/logo_sfr.svg"),
    Carrier("Telefónica",           "TF", Color(0xFF0065FD), "logos/logo_telefonica.svg"),
    Carrier("Tigo",                 "TG", Color(0xFF396AB3), "logos/logo_tigo.png"),
    Carrier("TIM",                  "TI", Color(0xFFE2002B), "logos/logo_tim.svg"),
    Carrier("Vodacom",              "VC", Color(0xFFDF2727), "logos/logo_vodacom.png"),
    Carrier("Vodafone",             "VF", Color(0xFFE40000), "logos/logo_vodafone.svg"),
    Carrier("Deutsche Telekom",     "DT", Color(0xFFE00079), "logos/logo_dt.svg"),
).associateBy { it.code }
