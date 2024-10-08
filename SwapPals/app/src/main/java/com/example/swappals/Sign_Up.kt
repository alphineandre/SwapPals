package com.example.swappals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Sign_Up : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth
        auth = Firebase.auth
        progress = findViewById(R.id.ProgressBar)

        val loginLink = findViewById<TextView>(R.id.LoginLink)
        loginLink.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.button4)
        registerButton.setOnClickListener {
            signUp()
            progress.visibility = View.GONE
        }
    }

    private fun signUp() {
        progress = findViewById(R.id.ProgressBar)
        progress.visibility = View.VISIBLE
        editTextEmail = findViewById(R.id.editTextTextEmailAddress2)
        editTextPassword = findViewById(R.id.editTextTextPassword2)

        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty()) {
            editTextEmail.error = "Field is empty, username or email required"
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Field can't be empty, password required"
            return
        }
        if (password.length < 7) {
            Toast.makeText(this, "Password should be at least 7 characters", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progress.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error occurred: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }
    }
}