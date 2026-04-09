package com.example.loginfirebase_25_26

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.trabajoredsocial.R
import com.example.trabajoredsocial.RepositorioUsuarios
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val repo = RepositorioUsuarios()
    val TAG = "Oscar"

    val isLoading = MutableStateFlow(false)
    val loginSuccess = MutableStateFlow(false)
    val errorMessage = MutableStateFlow<String?>(null)

    val isGoogleLogin = MutableStateFlow(false)


    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    fun loginWithEmail(email: String, password: String) {
        isLoading.value = true
        errorMessage.value = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    loginSuccess.value = true
                } else {
                    errorMessage.value = task.exception?.message
                }
            }
    }

    fun registerWithEmail(email: String, password: String, nombre: String, fotoUrl: String,rol:Int) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                repo.registrarUsuario(email, password, nombre, fotoUrl,rol)

                isLoading.value = false
                isGoogleLogin.value = false
                loginSuccess.value = true

            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = e.message
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        if (idToken.isEmpty()) return

        isLoading.value = true
        errorMessage.value = null

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    viewModelScope.launch {
                        try {
                            val user = auth.currentUser

                            if (user != null) {
                                repo.registraGmailAutentificado(
                                    uid = user.uid,
                                    nombre = user.displayName ?: "",
                                    email = user.email ?: "",
                                    fotoUrl = user.photoUrl?.toString() ?: "",
                                    rol = 2
                                )
                            }

                            isLoading.value = false
                            isGoogleLogin.value = true
                            loginSuccess.value = true

                        } catch (e: Exception) {
                            isLoading.value = false
                            errorMessage.value = e.message
                        }
                    }

                } else {
                    isLoading.value = false
                    errorMessage.value = task.exception?.message
                }
            }
    }

    fun signOut(context: Context) {
        auth.signOut()
        // Si el usuario se logueó con Google, cierra sesión y revoca acceso
        if (isGoogleLogin.value) {
            val googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(
                context,
                com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                    com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            )

            googleSignInClient.signOut().addOnCompleteListener {
                Log.d(TAG, "Google Sign-Out completado")
            }

            googleSignInClient.revokeAccess().addOnCompleteListener {
                Log.d(TAG, "Google Access revocado")
            }
        }

        // Reiniciar estados del ViewModel
        loginSuccess.value = false
        errorMessage.value = null
        isLoading.value = false
        isGoogleLogin.value = false

    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}


/*
** LiveData
    Ventajas

        Muy estable y probado: funciona con Activities, Fragments y Compose.

        Funciona automáticamente con el ciclo de vida (observe respeta LifecycleOwner).

        Fácil de usar si tu app todavía mezcla XML + Compose.

    Desventajas

        No tan flexible para flows de datos reactivos.

        Manejo de coroutines menos natural.

        Para Compose, necesitas observeAsState() cada vez que quieres usarlo como State.


** StateFlow / MutableStateFlow (o SharedFlow)
    Ventajas

        Integración nativa con Compose: collectAsState() convierte un StateFlow en State automáticamente.

        Funciona muy bien con coroutines, lo que hace más fácil manejar loading, errores o eventos.

        Evita problemas de “duplicación de eventos” que a veces tienes con LiveData (como Toast que se dispara varias veces al recomponer).

    Desventajas

        Necesitas un scope de coroutine para colectar.

        No tiene “respetar lifecycle” automático como LiveData: si quieres observar desde un Fragment/Activity, necesitas lifecycleScope.launchWhenStarted o similar.
 */