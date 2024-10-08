package com.example.swappals


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.view.isNotEmpty
import coil.load
import com.example.swappals.adapter.ItemAdapter
import com.example.swappals.databinding.ActivityAddItemBinding
import com.example.swappals.model.Category
import com.example.swappals.model.Item
import com.example.swappals.model.YourDataClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.Collection

class Add_item : AppCompatActivity() {

    private val items = mutableListOf<Item>()
    private lateinit var binding: ActivityAddItemBinding
    companion object {
        private const val GALLERY_REQUEST_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }
    private lateinit var achievement: ProgressBar
    private var itemCount = 0
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var currentUser: FirebaseUser

    private lateinit var editTextItemTitle: EditText
    private lateinit var editTextItemDescription: EditText
    private lateinit var imageViewItemImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        achievement = findViewById(R.id.progressBar2)

        val auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference
        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance()

        val listViewItems = findViewById<ListView>(R.id.listViewItems)
        val itemAdapter = ItemAdapter(this, items)

        binding.imageView.setOnClickListener {
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItem = arrayOf("Select photo from gallery", "Take a photo")
            pictureDialog.setItems(pictureDialogItem) { dialog, which ->

                when (which) {
                    0 -> galleryCheckPermission()
                    1 -> cameraCheckPermission()
                }
            }
            pictureDialog.show()
        }



        val categoryName = intent.getStringExtra("category")
        val categories = intent.getSerializableExtra("categories") as Array<Category>?

        if (categoryName == null || categories == null) {
            // Handle the case where the category name or categories array is not passed correctly
            // Show an error message or return to the previous activity
            return
        }

        val category = categories.find { it.name == categoryName }
        if (category == null) {
            // Handle the case where the category object is not found for the given category name
            // Show an error message or return to the previous activity
            return
        }

        //val categoryItems = items.filter { it.category == category }

        listViewItems.adapter = itemAdapter

        val btn_addItem = findViewById<Button>(R.id.btn_AddItem)

        //Setting the button in order to save the item
        btn_addItem.setOnClickListener {


            val editTextItemTitle = findViewById<EditText>(R.id.editTextItemTitle)
            val editTextItemDescription = findViewById<EditText>(R.id.editTextItemDescription)
            val imageViewItemImage = findViewById<ImageView>(R.id.imageView)
            val itemImage = (imageViewItemImage.drawable as BitmapDrawable).bitmap

            val itemTitle = editTextItemTitle.text.toString()
            val itemDescription = editTextItemDescription.text.toString()

            val item = Item(
                title = itemTitle,
                category = category,
                description = itemDescription,
                image = itemImage
            )

            items.add(item)
            // Notify the adapter that the data has changed.
            itemAdapter.notifyDataSetChanged()


            Toast.makeText(
                this,
                "Item added successfully to category: ${category.name}",
                Toast.LENGTH_SHORT
            ).show()

            achievement.setProgress(0)
            achievement.max = 10

            if (listViewItems.isNotEmpty()) {
                itemCount++
            }

            achievement.progress = itemCount

            //Checking if the achievement equals the number of items counted in order to grant an award
            if (achievement.progress == 1) {
                Toast.makeText(this, "You have achieved the Starter award for adding 1 item", Toast.LENGTH_SHORT)
                    .show()

            } else if (achievement.progress == 5) {
                Toast.makeText(this, "You have achieved the Collector award for adding 5 item", Toast.LENGTH_SHORT)
                    .show()

            } else if (achievement.progress == 10) {
                Toast.makeText(this, "You have achieved the Parkrat award for adding 10 item", Toast.LENGTH_SHORT)
                    .show()
            }
            saveItemToFirebase()
        }

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
        val databaseReference = database.child("All Categories")

        // Event listener to fetch data from the database node
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                for (itemSnapshot in snapshot.children) {
                    val yourDataClass = itemSnapshot.getValue(YourDataClass::class.java)
                    yourDataClass?.let {
                        val item = Item(
                            title = yourDataClass.title,
                            category = yourDataClass.category.toString(),
                            description = yourDataClass.description,
                            image = yourDataClass.image
                        )
                        items.add(item)
                    }
                }
                itemAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the onCancelled event
            }
        })
    }

    //Setting up the notification feature which will show an notification message in the device's navigation menu
    private fun showNotification(message: String) {
        val channelId = "achievement_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.swap_pals)
            .setContentTitle("Achievement Unlocked")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun galleryCheckPermission() {
        Dexter.withContext(this).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@Add_item, "You have denied the storage permission" +
                            "in order to select an image", Toast.LENGTH_SHORT
                ).show()

                showRotationalDialogForPermissiom()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showRotationalDialogForPermissiom()
            }


        }).onSameThread().check()
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    //Checking the permission for the camera, so that the usr is able to take a picture
    private fun cameraCheckPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    // Permission is granted, proceed with camera operation
                    camera()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@Add_item,
                        "Camera permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }


    private fun camera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                ///
                CAMERA_REQUEST_CODE->{

                    val bitmap = data?.extras?.get("data") as Bitmap

                    binding.imageView.load(bitmap)
                }
                GALLERY_REQUEST_CODE->{
                    binding.imageView.load(data?.data){}
                }

            }
        }
    }

    private fun  showRotationalDialogForPermissiom(){
        AlertDialog.Builder(this)
            .setMessage("It looks like you haven't allowed all the permissions"
                    +"required for this feature. To enable them go under the App settings of Swap Pals")


            .setPositiveButton("GO TO SETTINGS"){_,_->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                }catch(e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL"){dialog, _->
                dialog.dismiss()
            }.show()
    }
    private fun openApplicationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun saveItemToFirebase() {

        editTextItemTitle = findViewById(R.id.editTextItemTitle)
        editTextItemDescription = findViewById(R.id.editTextItemDescription)
        imageViewItemImage = findViewById(R.id.imageView)


        val itemTitle = editTextItemTitle.text.toString().trim()
        val itemDescription = editTextItemDescription.text.toString().trim()
        val imageDrawable = imageViewItemImage.drawable
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog: AlertDialog = builder.create()

        if (itemTitle.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        if (itemDescription.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageDrawable == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }
        val categoryName = intent.getStringExtra("category")
        val categories = intent.getSerializableExtra("categories") as Array<Category>?

        if (categoryName == null || categories == null) {
            // Handle the case where the category name or categories array is not passed correctly
            // Show an error message or return to the previous activity
            return
        }

        val category = categories.find { it.name == categoryName }
        if (category == null) {
            // Handle the case where the category object is not found for the given category name
            // Show an error message or return to the previous activity
            return
        }


        val imageBitmap = (imageDrawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val filename = UUID.randomUUID().toString()
        val imageRef = storage.reference.child("images/$filename.jpg")
        val user = Firebase.auth.currentUser
        val uploadTask = imageRef.putBytes(data)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val item = YourDataClass(itemTitle, itemDescription, downloadUri.toString(), category.toString())
                val itemId = database.child("Items").push().key
                if (itemId != null) {


                    database.child("All Categories").child(itemId).setValue(item)
                        .addOnCompleteListener { saveTask ->
                            if (saveTask.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Item saved successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                editTextItemTitle.setText("")
                                editTextItemDescription.setText("")
                            } else {
                                Toast.makeText(
                                    this,
                                    "Failed to save item",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                            }
                        }
                }
            } else {
                Toast.makeText(
                    this,
                    "Failed to upload image",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

        }
    }

}