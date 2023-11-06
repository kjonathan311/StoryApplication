package com.example.storyapplication.ui.story

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.storyapplication.databinding.ActivityAddStoryBinding
import com.example.storyapplication.utils.getImageUri
import com.example.storyapplication.utils.reduceFileImage
import com.example.storyapplication.utils.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel



class AddStoryActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddStoryBinding
    private val storyViewModel: StoryViewModel by viewModel()

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel.setLoading(false)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnCamera.setOnClickListener {startCamera()}
        binding.btnGallery.setOnClickListener {startGallery()}
        binding.btnStoryAdd.setOnClickListener {uploadStory()}

        storyViewModel.loading.observe(this) {
            showLoading(it)
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.ivPreviewImg)
        }
    }

    private fun uploadStory(){
        currentImageUri?.let {
            val imageFile = uriToFile(it, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = "${binding.edDescStory.text}"
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                lifecycleScope.launch {
                    storyViewModel.uploadStory(multipartBody,requestBody)
                }
                storyViewModel.checkUpload.observe(this){
                    if (it){
                        finish()
                    }
                }
            }catch (e:Exception){
                Log.e("error upload",e.toString())
            }
        }?:Toast.makeText(this,"Please insert an Image first",Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(load:Boolean){
        if (load){
            binding.pgAddStory.visibility= View.VISIBLE
        }else{
            binding.pgAddStory.visibility= View.GONE
        }
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}