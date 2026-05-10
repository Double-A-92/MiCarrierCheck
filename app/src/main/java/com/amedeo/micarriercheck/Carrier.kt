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
    
    // ===== Unverified =====
    Carrier("Airtel",               "AR", Color(0xFFEB1D24), "logos/logo_airtel.svg"),
    Carrier("AIS",                  "AI", Color(0xFFB2D51A), "logos/logo_ais.svg"),
    Carrier("Bell",                 "BL", Color(0xFF0066A3), "logos/logo_bell.png"),
    Carrier("China Mobile",         "CM", Color(0xFF0085CF), "logos/logo_chinamobile.svg"),
    Carrier("China Telecom",        "CT", Color(0xFF003176), "logos/logo_chinatelecom.svg"),
    Carrier("China Unicom",         "CU", Color(0xFF373B91), "logos/logo_chinaunicom.svg"),
    Carrier("Docomo",               "DO", Color(0xFFCC0033), "logos/logo_docomo.svg"),
    Carrier("EE",                   "EE", Color(0xFF1D9E9D), "logos/logo_ee.svg"),
    Carrier("Etisalat",             "ET", Color(0xFF78A22B), "logos/logo_etisalat.svg"),
    Carrier("Free Mobile",          "FM", Color(0xFFD91F26), "logos/logo_freemobile.svg"),
    Carrier("Globe Telecom",        "GB", Color(0xFF29348E), "logos/logo_globe.svg"),
    Carrier("Jio",                  "JI", Color(0xFF0A2884), "logos/logo_jio.svg"),
    Carrier("KT",                   "KT", Color(0xFFEC2227), "logos/logo_kt.svg"),
    Carrier("LG U+",                "LU", Color(0xFFBF0C3F), "logos/logo_lguplus.svg"),
    Carrier("Magenta Telekom",      "MG", Color(0xFFE00079), "logos/logo_dt.svg"),
    Carrier("Maxis",                "MX", Color(0xFF40C606), "logos/logo_maxis.svg"),
    Carrier("MEO",                  "ME", Color(0xFF2E26FD), "logos/logo_meo.svg"),
    Carrier("O2",                   "O2", Color(0xFF04256B), "logos/logo_o2.svg"),
    Carrier("Optus",                "OP", Color(0xFF3CA5AC), "logos/logo_optus.svg"),
    Carrier("Play",                 "PL", Color(0xFF644078), "logos/logo_play.svg"),
    Carrier("Proximus",             "PX", Color(0xFF5A2D8F), "logos/logo_proximus.svg"),
    Carrier("Rogers",               "RG", Color(0xFFDA291C), "logos/logo_rogers.svg"),
    Carrier("SK Telecom",           "SK", Color(0xFFE8002C), "logos/logo_sktelecom.svg"),
    Carrier("Swisscom",             "SC", Color(0xFF003788), "logos/logo_swisscom.svg"),
    Carrier("Sunrise",              "SU", Color(0xFFD9291C), "logos/logo_sunrise.svg"),
    Carrier("T-Mobile",             "TM", Color(0xFFE00079), "logos/logo_dt.svg"),
    Carrier("Telstra",              "TS", Color(0xFF0D54FD), "logos/logo_telstra.svg"),
    Carrier("Telus",                "TL", Color(0xFF4B286C), "logos/logo_telus.svg"),
    Carrier("TrueMove H",           "TH", Color(0xFFDD002B), "logos/logo_truemove.svg"),
    Carrier("Turkcell",             "TC", Color(0xFFFDC30C), "logos/logo_turkcell.svg"),
    Carrier("Verizon",              "VZ", Color(0xFFCD040B), "logos/logo_verizon.svg"),
    Carrier("Viettel",              "VT", Color(0xFFEC0033), "logos/logo_viettel.svg"),
    Carrier("WindTre",              "WT", Color(0xFFFD6800), "logos/logo_windtre.svg"),
    Carrier("Zain",                 "ZA", Color(0xFF7E8184), "logos/logo_zain.svg"),
).associateBy { it.code }
