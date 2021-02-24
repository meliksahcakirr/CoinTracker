package com.meliksahcakir.cointracker.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object UserRemote {

    var firebaseAuth = FirebaseAuth.getInstance()
    var favorites = mutableListOf<String>()

    lateinit var userRef: DocumentReference

    suspend fun setup() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        userRef = Firebase.firestore.collection("users").document(uid)
        getFavoriteList()?.let {
            favorites = it.toMutableList()
        }
    }

    private suspend fun getFavoriteList(): List<String>? {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = userRef.get().await()
                snapshot.get("favorites") as? List<String>
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun addToFavoriteList(coinId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                favorites.add(coinId)
                val data = HashMap<String, List<String>>()
                data["favorites"] = favorites
                userRef.set(data)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun removeFromFavoriteList(coinId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                favorites.remove(coinId)
                val data = HashMap<String, List<String>>()
                data["favorites"] = favorites
                userRef.set(data)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
