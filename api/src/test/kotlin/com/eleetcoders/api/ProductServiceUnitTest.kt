package com.eleetcoders.api

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.services.ProductService
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.*
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.JsonParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ProductServiceUnitTest {

//    @Mock
//    private lateinit var firebaseApp: FirebaseApp
//
//    @Mock
//    private lateinit var firestore: Firestore
//
//    @Mock
//    private lateinit var collectionReference: CollectionReference
//
//    @Mock
//    private lateinit var querySnapshot: QuerySnapshot
//
//    @Mock
//    private lateinit var documentSnapshot: QueryDocumentSnapshot
//
//    @InjectMocks

//    @Mock
//    private lateinit var firestoreMock: Firestore
//
//    @InjectMocks
    private lateinit var productService: ProductService

    @BeforeEach
    fun setup() {
        //MockitoAnnotations.openMocks(this)
        productService = ProductService()
    }

    @Test
    fun `should return all products when no category`() {
//        val firestoreOptionsMock: FirestoreOptions = mock(FirestoreOptions::class.java)
//        `when`(firestoreOptionsMock.getService()).thenReturn(firestoreMock)
        mockStatic(FirestoreClient::class.java)
        val dbMock: Firestore = mock(Firestore::class.java)
        val collectionReference1: CollectionReference = mock(CollectionReference::class.java)
        val collectionReference2: CollectionReference = mock(CollectionReference::class.java)

        val collectionsMock: List<CollectionReference> = listOf(collectionReference1, collectionReference2)

        `when`(FirestoreClient.getFirestore()).thenReturn(dbMock)  //equivelent to putting firestoreMock from annotation
        `when`(dbMock.listCollections()).thenReturn(collectionsMock)

        val collectionName1: String = "BOOK"
        val collectionName2: String = "user"
        `when`(collectionReference1.id).thenReturn(collectionName1)
        `when`(collectionReference2.id).thenReturn(collectionName2)

        val documentSnapshotMock1: QueryDocumentSnapshot = mock(QueryDocumentSnapshot::class.java)
        val documentSnapshotMock2: QueryDocumentSnapshot = mock(QueryDocumentSnapshot::class.java)
        val productData1 = mutableMapOf<String, Any>()
        productData1["price"] = 10.0
        productData1["desc"] = "Product description"
        val productData2 = mutableMapOf<String, Any>()
        productData2["email"] = "urmom@gmail.com"
        productData2["password"] = "testing123"

        val apiFuture1: ApiFuture<QuerySnapshot> = mock(ApiFuture::class.java) as ApiFuture<QuerySnapshot>
        val apiFuture2: ApiFuture<QuerySnapshot> = mock(ApiFuture::class.java) as ApiFuture<QuerySnapshot>
        val querySnapshotMock1: QuerySnapshot = mock(QuerySnapshot::class.java)
        val querySnapshotMock2: QuerySnapshot = mock(QuerySnapshot::class.java)

        `when`(collectionReference1.get()).thenReturn(apiFuture1)
        `when`(apiFuture1.get()).thenReturn(querySnapshotMock1)
        `when`(querySnapshotMock1.documents).thenReturn(listOf(documentSnapshotMock1))

        `when`(collectionReference2.get()).thenReturn(apiFuture2)
        `when`(apiFuture2.get()).thenReturn(querySnapshotMock2)
        `when`(querySnapshotMock2.documents).thenReturn(listOf(documentSnapshotMock2))

        `when`(documentSnapshotMock1.data).thenReturn(productData1)
        `when`(documentSnapshotMock1.id).thenReturn("123")
        `when`(documentSnapshotMock2.data).thenReturn(productData2)
        `when`(documentSnapshotMock2.id).thenReturn("456")


        val productCompanionMock: Product.Companion = mock(Product.Companion::class.java)
        `when`(productCompanionMock.ignoreCase(collectionName1)).thenReturn(Product.Category.BOOK)
        `when`(productCompanionMock.ignoreCase(collectionName2)).thenReturn(Product.Category.TECH)




        // Call the method you want to test
        val responseString = productService.getAllProducts()
        val jsonResponse = JsonParser.parseString(responseString).asJsonObject
        verify(dbMock).listCollections()

        Assertions.assertTrue(jsonResponse.has("BOOK"))
        Assertions.assertFalse(jsonResponse.has("user"))
    }



}