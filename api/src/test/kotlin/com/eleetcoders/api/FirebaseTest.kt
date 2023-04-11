package com.eleetcoders.api

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FirebaseTest {
    @Test
    fun serviceKeyTest() {
        val serviceAccount = FirebaseTest::class.java.getResourceAsStream("/serviceAccountKey.json")
        assertNotNull(serviceAccount)
    }
}