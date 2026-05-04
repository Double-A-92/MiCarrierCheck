package com.amedeo.micarriercheck

import androidx.core.net.toUri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.amedeo.micarriercheck.BuildConfig
import com.materialkolor.dynamicColorScheme


// ---------------------------------------------------------------------------
// Image loader — registered once per composition, shared across all badges.
// The SvgDecoder.Factory() teaches Coil how to rasterise SVG assets.
// ---------------------------------------------------------------------------
@Composable
private fun rememberSvgImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
}

// All states the debug button cycles through:
// index 0 = Xiaomi (unlocked), indices 1..N = each known carrier (locked).
private val DEBUG_CARRIERS: List<DetectionResult> = buildList {
    add(DetectionResult(isCarrierLocked = false, carrier = XIAOMI_DEFAULT, rawProperties = emptyMap()))
    CARRIER_REGISTRY.values.forEach { carrier ->
        add(DetectionResult(isCarrierLocked = true, carrier = carrier, rawProperties = emptyMap()))
    }
}

// ---------------------------------------------------------------------------
// Root composable: applies dynamic theme based on the detected carrier.
// ---------------------------------------------------------------------------
@Composable
fun CarrierApp(realResult: DetectionResult) {
    // Debug override — only compiled/active in debug builds
    var debugIndex by remember { mutableIntStateOf(-1) }  // -1 = use real result
    val result = if (BuildConfig.DEBUG && debugIndex >= 0) DEBUG_CARRIERS[debugIndex] else realResult

    val targetColor = if (result.isCarrierLocked) result.carrier.brandColor else XIAOMI_DEFAULT.brandColor
    val primaryColor by animateColorAsState(targetValue = targetColor, animationSpec = tween(400), label = "primary_color")
    val colorScheme = dynamicColorScheme(
        seedColor = primaryColor,
        isDark = false,
        modifyColorScheme = { generatedScheme ->
            generatedScheme.copy(primary = primaryColor)
        }
    )

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CarrierInfoScreen(
                result = result,
                onDebugCycle = if (BuildConfig.DEBUG) {{
                    debugIndex = ((if (debugIndex < 0) -1 else debugIndex) + 1) % DEBUG_CARRIERS.size
                }} else null
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Main screen layout
// ---------------------------------------------------------------------------
@Composable
fun CarrierInfoScreen(result: DetectionResult, onDebugCycle: (() -> Unit)? = null) {
    var detailsExpanded by remember { mutableStateOf(false) }
    val imageLoader = rememberSvgImageLoader()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── Debug cycle button (debug builds only) ─────────────────────────
        if (onDebugCycle != null) {
            TextButton(
                onClick = onDebugCycle,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "⟳  ${result.carrier.displayName}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Spacer(modifier = Modifier.height(32.dp))
        }

        // ── Hero ──────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CarrierLogoBadge(
                carrier = if (result.isCarrierLocked) result.carrier else XIAOMI_DEFAULT,
                imageLoader = imageLoader
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (result.isCarrierLocked) "Carrier Locked" else "Unlocked",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (result.isCarrierLocked)
                    "OTA Updates controlled by ${result.carrier.displayName}"
                else
                    "Standard MIUI / HyperOS",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }

        // ── Expandable details panel ───────────────────────────────────────
        PropertiesPanel(
            properties = result.rawProperties,
            expanded = detailsExpanded,
            onToggle = { detailsExpanded = !detailsExpanded }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ---------------------------------------------------------------------------
// Carrier logo badge
// Shows the real carrier/Xiaomi logo inside a white circle with a brand-
// coloured border. Falls back to a large initial letter or a lock icon.
// ---------------------------------------------------------------------------
@Composable
private fun CarrierLogoBadge(carrier: Carrier, imageLoader: ImageLoader) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(width = 4.dp, color = primaryColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (carrier.logoAssetPath != null) {
            val uri = "file:///android_asset/${carrier.logoAssetPath}".toUri()
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(uri)
                    .crossfade(true)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = carrier.displayName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(110.dp)
                    .padding(8.dp)
            )
        } else if (carrier.code in CARRIER_REGISTRY) {
            // Known carrier, no logo asset — show first letter in brand colour
            Text(
                text = carrier.displayName.first().uppercase(),
                color = primaryColor,
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif
            )
        } else {
            // Completely unknown carrier
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "Carrier locked",
                tint = primaryColor,
                modifier = Modifier.size(72.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Expandable raw system properties panel
// ---------------------------------------------------------------------------
@Composable
private fun PropertiesPanel(
    properties: Map<String, String>,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    val arrowAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrow_angle"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onToggle),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "System Properties",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.rotate(arrowAngle)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    properties.forEach { (key, value) ->
                        PropertyRow(key = key, value = value)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyRow(key: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = key,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value.ifEmpty { "[Empty]" },
            style = MaterialTheme.typography.bodyLarge,
            color = if (value.isEmpty()) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
