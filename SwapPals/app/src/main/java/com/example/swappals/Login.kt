package com.example.swappals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth



        val RegisterLink = findViewById<TextView>(R.id.registerLink)
        RegisterLink.setOnClickListener {
            val intent = Intent(this,Sign_Up::class.java)
            startActivity(intent)
            finish()
        }

        val loginBtn = findViewById<Button>(R.id.btn_Login)
        loginBtn.setOnClickListener() {
           login()
        }

    }
    private fun login()
    {
        progress = findViewById(R.id.ProgressBar)
        progress.visibility = View.VISIBLE
        editTextEmail = findViewById(R.id.editTextTextEmailAddress)
        editTextPassword = findViewById(R.id.editTextTextPassword)



        val inputEmail = editTextEmail.text.toString()
        val inputPassword = editTextPassword.text.toString()
        if (inputEmail.isEmpty()){
            editTextEmail.error = "Field is empty, username or email required"
            return
        }

        if (inputPassword.isEmpty()){
            editTextPassword.error = "Field can't be empty, password required"
            return
        }

        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this,User_Dashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Incorrect Username or Password",Toast.LENGTH_SHORT).show()

                }
            }.addOnFailureListener{
                Toast.makeText(this,"error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}