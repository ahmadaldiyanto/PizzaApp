package com.example.pizzaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtEmail : EditText = findViewById(R.id.registerEmail)
        val txtName : EditText = findViewById(R.id.registerPersonName)
        val txtLevel : EditText = findViewById(R.id.registerLevel)
        val txtPassword : EditText = findViewById(R.id.registerPassword)
        //instance button register
        val btnRegister : Button = findViewById(R.id.buttonRegisterAccount)

        btnRegister.setOnClickListener {
            //object class database helper
            val databaseHelper = DatabaseHelper ( this )
            //declare data
            val email : String = txtEmail.text.toString().trim()
            val name : String = txtName.text.toString().trim()
            val level : String = txtLevel.text.toString().trim()
            val password : String = txtPassword.text.toString().trim()

            //check data > email sudah terdaftqar atau belum
            val data:String = databaseHelper.checkData(email)
            //jika belum terdaftar
            if(data == "") {
                //insert data
                databaseHelper.addAccount(
                    email,name,level,password)

                //show login activity
                val intentlogin = Intent (this@RegisterActivity, LoginActivity::class.java)
                startActivity(intentlogin)
            }else {
                //jika email telah terdaftar
                Toast.makeText(this@RegisterActivity, "Register failed. " + "Your email alredy registered", Toast.LENGTH_SHORT).show()
            }
        }
    }
}