package ua.ideabank.obank.core.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

class IntentUtils {

    companion object {
        fun openUrl(context: Context, url: String) {
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun openTelegram(context: Context, name: String) {
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/$name"))
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun openViber(context: Context, number: String) {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("viber://add?number=$number")
                ).apply {
                    setPackage("com.viber.voip")
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun openCallPhone(context: Context, phone: String) {
            val intent = Intent(
                Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)
            )
            context.startActivity(intent)
        }

        fun openWebPage(context: Context, url: String) {
            var urlTmp = url
            if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
                urlTmp = "$HTTP $url"
            }
            val webpage: Uri = Uri.parse(urlTmp)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            context.startActivity(intent)
        }

        private const val HTTPS = "https://"
        private const val HTTP = "http://"
    }
}