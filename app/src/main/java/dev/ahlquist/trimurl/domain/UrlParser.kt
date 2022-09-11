package dev.ahlquist.trimurl.domain

import android.net.Uri
import dev.ahlquist.trimurl.model.UrlPart

const val NO_PORT = -1

class UrlParser {

    fun parse(urlString: String): List<UrlPart> {
        return try {
            _parse(urlString)
        } catch (e: Throwable) {
            listOf( UrlPart.Unparseable(urlString, e) )
        }
    }

    private fun _parse(urlString: String): List<UrlPart> {
        val result = mutableListOf<UrlPart>()
        val url = Uri.parse(urlString)

        try {
            result.add(
                UrlPart.SchemeAndAuthority(
                    url.scheme!!,
                    url.host!!,
                    if (url.port != NO_PORT) url.port else null
                )
            )
        } catch (e: NullPointerException) {
            // No Scheme: android.net.Uri will attach it as a path segment in this case. So I do nothing.
            // No Host: android.net.Uri will insert a blank host, so the try will succeed.
            // TODO logging
        }

        url.pathSegments.forEach { segment ->
            result.add(UrlPart.PathSegment(segment))
        }

        url.queryParameterNames.forEach { name ->
            val value = url.getQueryParameter(name)
            result.add(UrlPart.QueryParam(name, value ?: ""))
        }

        url.fragment?.let { fragment ->
            result.add(UrlPart.FragmentId(fragment))
        }

        return result
    }
}
