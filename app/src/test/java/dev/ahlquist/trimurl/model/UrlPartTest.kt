package dev.ahlquist.trimurl.model

import org.junit.Assert.*
import org.junit.Test

class UrlPartTest {

    @Test
    fun `CONSTRUCT a SchemeAndAuthority with scheme and host`(){
        val subject = UrlPart.SchemeAndAuthority("https", "www.example.com")
        assertEquals("https://www.example.com", subject.displayString)
    }

    @Test
    fun `CONSTRUCT a SchemeAndAuthority with scheme, host and port`(){
        val subject = UrlPart.SchemeAndAuthority("https", "www.example.com", 420)
        assertEquals("https://www.example.com:420", subject.displayString)
    }

    @Test
    fun `CONSTRUCT a QueryParam`(){
        val subject = UrlPart.QueryParam("key", "value")
        assertEquals("key=value", subject.displayString)
    }
}