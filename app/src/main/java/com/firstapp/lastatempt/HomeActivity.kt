package com.firstapp.lastatempt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var buttonLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize logout button
        buttonLogout = findViewById(R.id.buttonLogout)

        // Set click listener
        buttonLogout.setOnClickListener {
            // Sign out the user
            FirebaseAuth.getInstance().signOut()
            // Navigate back to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
