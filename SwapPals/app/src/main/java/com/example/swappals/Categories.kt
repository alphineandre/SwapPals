package com.example.swappals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.swappals.adapter.CategoryAdapter
import com.example.swappals.model.Category
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlin.collections.Collection

class Categories : AppCompatActivity() {
    private val categories = mutableListOf<Category>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseReference = FirebaseDatabase.getInstance().getReference("Category")
        setContentView(R.layout.activity_categories)

        val btn_addCategory = findViewById<FloatingActionButton>(R.id.addCategory)
        val listViewCategories = findViewById<ListView>(R.id.listViewCategories)

        categoryAdapter = CategoryAdapter(this, categories)
        listViewCategories.adapter = categoryAdapter

        btn_addCategory.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val v = inflater.inflate(R.layout.add_category, null)
            val editTextCategoryName = v.findViewById<EditText>(R.id.editTextCategoryName)
            val editTextSetGoal = v.findViewById<EditText>(R.id.editTextSetGoal)
            val addDialog = AlertDialog.Builder(this)
            addDialog.setView(v)
            addDialog.setPositiveButton("Create") { dialog, _ ->
                val categoryName = editTextCategoryName.text.toString()
                val setGoal = editTextSetGoal.text.toString()

                // Validate if the field is empty
                if (categoryName.isEmpty()) {
                    editTextCategoryName.error = "Field can't be empty, Category Required"
                    return@setPositiveButton
                }

                val goalItemCount = setGoal.toIntOrNull()
                if (goalItemCount == null || goalItemCount <= 0) {
                    editTextSetGoal.error = "Invalid goal, please set a goal!!!"
                    return@setPositiveButton
                }

                // Create a new category object.
                val category = Category(name = categoryName, goalItem = goalItemCount,)

                // Generate a unique key for the category
                val categoryKey = databaseReference.push().key

                categoryKey?.let {
                    // Store the category object in the database using the generated key
                    databaseReference.child(it).setValue(category)
                        .addOnSuccessListener {
                            // Display a message to the user.
                            Toast.makeText(
                                this, "Category created successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { exception ->
                            // Handle any error that occurred while saving the category.
                            Toast.makeText(
                                this, "Failed to create category: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

                // Clear the category name
                editTextCategoryName.setText("")
                // Clear the goal set
                editTextSetGoal.setText("")

                dialog.dismiss()
            }

            addDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }

            addDialog.create()
            addDialog.show()
        }

        listViewCategories.setOnItemClickListener { parent, view, position, id ->
            val category = categories[position].name

            // You can pass the selected category to the ItemActivity using Intent and startActivity
            // Example:
            val intent = Intent(this, Add_item::class.java)
            intent.putExtra("category", category)
            intent.putExtra("categories", categories.toTypedArray())
            startActivity(intent)
        }

        // Call loadCategories after initializing the categoryAdapter
        loadCategories()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.bottom_add
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(applicationContext, User_Dashboard::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                R.id.bottom_collection -> {
                    startActivity(Intent(applicationContext, Collection::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                R.id.bottom_add -> {
                    startActivity(Intent(applicationContext, Categories::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                R.id.bottom_profile -> {
                    startActivity(Intent(applicationContext, Profile::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                R.id.bottom_logout -> {
                    startActivity(Intent(applicationContext, Login::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                else -> false
            }
        }
    }

    private fun loadCategories() {
        eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categories.clear()
                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(Category::class.java)
                    category?.let { categories.add(it) }
                }
                categoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any error that occurred while fetching categories.
                Toast.makeText(
                    this@Categories, "Failed to load categories: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        databaseReference.addValueEventListener(eventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(eventListener)
    }
}