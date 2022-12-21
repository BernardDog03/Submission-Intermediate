package com.example.submissionintermediate1.ui.details

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent.EXTRA_USER
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.submissionintermediate1.data.database.StoryResponseItem
import com.example.submissionintermediate1.data.response.Stories
import com.example.submissionintermediate1.databinding.ActivityDetailsBinding
import com.example.submissionintermediate1.utils.DateFormat.setLocalTime
import java.util.Objects

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<StoryResponseItem>(EXTRA_STORY)
        detailStory(story)
    }

    private fun detailStory(story: StoryResponseItem?) {
        binding.apply {
            if (story != null) {
                tvDateDetails.text = setLocalTime(story.createdAt.toString())
                tvUsernameDetails.text = story.name
                tvDescriptionDetails.text = story.description
                Glide
                    .with(this@DetailsActivity)
                    .load(story.photoUrl)
                    .into(imgDetails)
            }
        }
    }

    companion object{
        const val EXTRA_STORY = "extra_story"
    }
}