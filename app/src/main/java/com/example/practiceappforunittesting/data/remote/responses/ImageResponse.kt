package com.example.practiceappforunittesting.data.remote.responses

data class ImageResponse(
    val hits: List<ImageResult>, // these are the actual images that we got from API
    val total: Int,
    val totalHits: Int // this is simply number of images we got from API
)