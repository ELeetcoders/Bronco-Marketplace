package com.eleetcoders.api

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc

@SpringBootTest
//@AutoConfigureMockMvc
class ApiApplicationTests {

	@Test
	fun testCollections() {
		val db: Firestore = FirestoreClient.getFirestore()
		val collections = db.listCollections()

		// Assert that the collections variable is not null.
		assertNotNull(collections)
		println(collections)

		// Assert that the collections variable contains the expected collections.
		val expectedCollections = listOf("TECH", "BOOK", "SERVICES", "chats", "user")
		assertThat(collections.map { it.id }).containsAll(expectedCollections)
	}





}
