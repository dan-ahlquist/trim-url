package dev.ahlquist.trimurl.model

sealed class UrlPart(
    val displayString: String
) {
    data class SchemeAndAuthority(
        val scheme: String,
        val host: String,
        val port: Int? = null,
    ): UrlPart(
        if (port!=null) "$scheme://$host:$port" else "$scheme://$host"
    )

    class PathSegment(displayString: String): UrlPart(displayString)

    data class QueryParam(
        val name: String,
        val value: String,
    ): UrlPart("$name=$value")

    class FragmentId(displayString: String): UrlPart(displayString)

    /**
     *  A default for when we just can't parse part of the URL
     */
    class Unparseable(displayString: String, val e: Throwable): UrlPart(displayString)
}
