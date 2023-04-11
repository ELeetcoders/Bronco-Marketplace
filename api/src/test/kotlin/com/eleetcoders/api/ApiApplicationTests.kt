package com.eleetcoders.api

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.assertj.core.api.Assertions.assertThat

@SpringBootTest
class ApiApplicationTests {

	@Test
	fun testCollections() {
		val db: Firestore = FirestoreClient.getFirestore()
		val collections = db.listCollections()

		// Assert that the collections variable is not null.
		assertNotNull(collections)

		// Assert that the collections variable contains the expected collections.
		val expectedCollections = listOf("user", "book", "category", "chat", "messages")
		assertThat(collections.map { it.id }).containsAll(expectedCollections)
	}





}
