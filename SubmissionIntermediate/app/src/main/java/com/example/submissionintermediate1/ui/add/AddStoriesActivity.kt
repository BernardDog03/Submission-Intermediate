package com.example.submissionintermediate1.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.R
import com.example.submissionintermediate1.databinding.ActivityAddStoriesBinding
import com.example.submissionintermediate1.ui.ViewModelFactory
import com.example.submissionintermediate1.ui.camera.CameraActivity
import com.example.submissionintermediate1.ui.main.MainViewModel
import com.example.submissionintermediate1.utils.reduceFileImage
import com.example.submissionintermediate1.utils.rotateBitmap
import com.example.submissionintermediate1.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@OptIn(ExperimentalPagingApi::class)
class AddStoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoriesBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { factory }
    private var getFile: File? = null
    private var location: Location? = null
    private var token: String = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.error),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.add_stories)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        factory = ViewModelFactory.getInstance(this)
        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.getToken().collect {
                    if (!it.isNullOrEmpty()) token = it
                }
            }
        }
        binding.apply {
            edDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setButtonEnable()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            imgPreview.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> setButtonEnable() }
            storyCamera.setOnClickListener {
                startCamera()
            }
            storyGallery.setOnClickListener {
                startGallery()

            }
            buttonUpload.setOnClickListener {
                uploadStory()
            }
            swLocation.setOnCheckedChangeListener { _, isCheck ->
                if (isCheck){
                    getLocation()
                }else{
                    this@AddStoriesActivity.location = null
                }
            }
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_COARSE_LOCATION
            )== PackageManager.PERMISSION_GRANTED
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it!= null){
                    this.location = it
                    Log.d("LOC", location.toString())

                }else{
                    toastMsg(getString(R.string.location_not_found))
                    binding.swLocation.isChecked = false
                }
            }
        }
        else{
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }
    private val requestPermissionLauncher= registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        when {
            isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getLocation()
            }
            else -> {

                binding.swLocation.isChecked = false
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.imgPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoriesActivity)
            getFile = myFile
            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    private fun uploadStory() {
        val file = reduceFileImage(getFile as File)
        val description =
            binding.edDescription.text.toString().toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        var lat: RequestBody? = null
        var lon: RequestBody? = null
        if (location !=null) {
            lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
            lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
        }
        lifecycleScope.launchWhenCreated {
            launch {

                viewModel.uploadStory(token, imageMultipart, description,lat,lon).collect {
                    it.onSuccess {
                        toastMsg(getString(R.string.story_upload))
                        finish()
                        showLoading(false)
                    }
                    it.onFailure {
                        toastMsg(getString(R.string.error_upload))
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressAdd.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setButtonEnable() {
        binding.apply {
            val imageView = getFile
            val description = edDescription.text
            buttonUpload.isEnabled = imageView != null && description.toString().isNotEmpty()
        }
    }

    private fun toastMsg(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


}