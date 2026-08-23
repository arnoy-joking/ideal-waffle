package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.BdiptvCyan
import com.example.ui.theme.BdiptvGreenPrimary

@Composable
fun WebPortalScreen(
  modifier: Modifier = Modifier,
  initialUrl: String = "http://tv.bdiptv.net"
) {
  val context = LocalContext.current
  var currentUrl by remember { mutableStateOf(initialUrl) }
  var webViewInstance by remember { mutableStateOf<WebView?>(null) }
  var canGoBack by remember { mutableStateOf(false) }
  var canGoForward by remember { mutableStateOf(false) }
  var loadingProgress by remember { mutableStateOf(0) }
  var isLoading by remember { mutableStateOf(true) }
  var isDesktopMode by remember { mutableStateOf(false) }
  var hasLoadError by remember { mutableStateOf(false) }

  Column(modifier = modifier.fillMaxSize()) {
    // Web Navigation Toolbar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 4.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          IconButton(
            onClick = {
              if (webViewInstance?.canGoBack() == true) {
                webViewInstance?.goBack()
              }
            },
            enabled = canGoBack,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
              tint = if (canGoBack) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
          }

          IconButton(
            onClick = {
              if (webViewInstance?.canGoForward() == true) {
                webViewInstance?.goForward()
              }
            },
            enabled = canGoForward,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.ArrowForward,
              contentDescription = "Forward",
              tint = if (canGoForward) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
          }

          IconButton(
            onClick = {
              hasLoadError = false
              webViewInstance?.reload()
            },
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Refresh",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }

          // Address Pill
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
              .weight(1f)
              .height(36.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Security",
                tint = BdiptvGreenPrimary,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = currentUrl,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                maxLines = 1
              )
            }
          }

          // Toggle Desktop Mode
          IconButton(
            onClick = {
              isDesktopMode = !isDesktopMode
              webViewInstance?.settings?.let { settings ->
                if (isDesktopMode) {
                  settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                } else {
                  settings.userAgentString = null
                }
                webViewInstance?.reload()
              }
            },
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = if (isDesktopMode) Icons.Default.DesktopWindows else Icons.Default.PhoneAndroid,
              contentDescription = "Desktop Mode Toggle",
              tint = if (isDesktopMode) BdiptvCyan else MaterialTheme.colorScheme.onSurface
            )
          }

          // Open in External Browser
          IconButton(
            onClick = {
              try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                context.startActivity(intent)
              } catch (e: Exception) {
                // Ignore if browser intent fails
              }
            },
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.OpenInBrowser,
              contentDescription = "Open in Chrome/Browser",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        // Loading Progress Indicator
        AnimatedVisibility(visible = isLoading && loadingProgress < 100) {
          LinearProgressIndicator(
            progress = loadingProgress / 100f,
            color = BdiptvGreenPrimary,
            trackColor = Color.Transparent,
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp)
          )
        }
      }
    }

    // WebView Container
    Box(modifier = Modifier.fillMaxSize()) {
      AndroidView(
        modifier = Modifier
          .fillMaxSize()
          .testTag("portal_webview"),
        factory = { ctx ->
          WebView(ctx).apply {
            settings.apply {
              javaScriptEnabled = true
              domStorageEnabled = true
              loadWithOverviewMode = true
              useWideViewPort = true
              mediaPlaybackRequiresUserGesture = false
              mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
              cacheMode = WebSettings.LOAD_DEFAULT
            }

            webViewClient = object : WebViewClient() {
              override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isLoading = false
                currentUrl = url ?: initialUrl
                canGoBack = view?.canGoBack() ?: false
                canGoForward = view?.canGoForward() ?: false
              }

              override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
              ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                  hasLoadError = true
                }
              }
            }

            webChromeClient = object : WebChromeClient() {
              override fun onProgressChanged(view: WebView?, newProgress: Int) {
                loadingProgress = newProgress
                isLoading = newProgress < 100
                canGoBack = view?.canGoBack() ?: false
                canGoForward = view?.canGoForward() ?: false
              }
            }

            webViewInstance = this
            loadUrl(initialUrl)
          }
        },
        update = { webView ->
          webViewInstance = webView
        }
      )

      if (hasLoadError) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Text(
              text = "tv.bdiptv.net Portal",
              style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
              color = BdiptvGreenPrimary
            )
            Text(
              text = "The web portal requires an active BDIX / local broadband network connection or HTTPS proxy.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
              onClick = {
                hasLoadError = false
                webViewInstance?.loadUrl(initialUrl)
              },
              colors = ButtonDefaults.buttonColors(containerColor = BdiptvGreenPrimary)
            ) {
              Text("Retry Loading Portal", color = Color.Black)
            }
          }
        }
      }
    }
  }
}
