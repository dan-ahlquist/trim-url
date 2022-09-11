package dev.ahlquist.trimurl.domain

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.ahlquist.trimurl.model.UrlPart
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UrlParserTest {

    // Note that this has to be an emulated test, because the underlying URI parser
    // is `android.net.Uri` which is not available to JVM unit tests.

    val subject: UrlParser
        get() { return UrlParser() }

    @Test
    fun testParse_simple_validate_SchemeAndAuthority() {
        val parsed = subject.parse("https://www.example.com/")

        assertEquals(1, parsed.size)
        val schemeAndAuthority = parsed[0] as UrlPart.SchemeAndAuthority
        assertEquals("https", schemeAndAuthority.scheme)
        assertEquals("www.example.com", schemeAndAuthority.host)
        assertNull(schemeAndAuthority.port)
    }

    @Test
    fun testParse_url_with_everything() {
        val parsed = subject.parse("http://ahlquist.dev/yo/how?you=doin#aight")

        assertEquals(5, parsed.size)

        val schemeAndAuthority = parsed[0] as UrlPart.SchemeAndAuthority
        assertEquals("http", schemeAndAuthority.scheme)
        assertEquals("ahlquist.dev", schemeAndAuthority.host)
        assertNull(schemeAndAuthority.port)

        val segment1 = parsed[1] as UrlPart.PathSegment
        assertEquals("yo", segment1.displayString)

        val segment2 = parsed[2] as UrlPart.PathSegment
        assertEquals("how", segment2.displayString)

        val queryParam = parsed[3] as UrlPart.QueryParam
        assertEquals("you", queryParam.name)
        assertEquals("doin", queryParam.value)

        val fragmentId = parsed[4] as UrlPart.FragmentId
        assertEquals("aight", fragmentId.displayString)
    }

/* NEGATIVE TEST CASES / ERROR HANDLING */

    @Test
    fun testParse_invalid_URL_fallback() {
        val parsed = subject.parse("https:??wrong.com)oops")

        assertEquals(1, parsed.size)
        val unparseable = parsed[0] as UrlPart.Unparseable
        assertEquals("https:??wrong.com)oops", unparseable.displayString)
        assertNotNull(unparseable.e)
    }


    @Test
    fun testParse_no_scheme_fallback() {
        val parsed = subject.parse("wrong.com/oops")

        assertEquals(2, parsed.size)
        val host = parsed[0] as UrlPart.PathSegment // This is what android.net.Uri does. I might tinker with it in the future.
        assertEquals("wrong.com", host.displayString)

        val segment1 = parsed[1] as UrlPart.PathSegment
        assertEquals("oops", segment1.displayString)
    }

    @Test
    fun testParse_no_host_fallback() {
        val parsed = subject.parse("http:///oops")

        assertEquals(2, parsed.size)

        val schemeAndAuthority = parsed[0] as UrlPart.SchemeAndAuthority
        assertEquals("http", schemeAndAuthority.scheme)
        assertEquals("", schemeAndAuthority.host)
        assertNull(schemeAndAuthority.port)
        assertEquals("http://", schemeAndAuthority.displayString)

        val segment1 = parsed[1] as UrlPart.PathSegment
        assertEquals("oops", segment1.displayString)
    }
}
