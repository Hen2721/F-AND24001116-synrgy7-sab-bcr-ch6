package com.example.presentation.workmanager

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.presentation.helper.worker.KEY_IMAGE_URI
import com.example.presentation.helper.worker.blurBitmap
import com.example.presentation.helper.worker.makeStatusNotification
import com.example.presentation.helper.worker.writeBitmapToFile

class BlurWorker(private val appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        makeStatusNotification("Blurring image", appContext)

        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val picture = BitmapFactory.decodeStream(
                appContext.contentResolver.openInputStream(Uri.parse(resourceUri))
            )
            val output = blurBitmap(picture, appContext)
            val outputUri = writeBitmapToFile(appContext, output)

            makeStatusNotification("Output is $outputUri", appContext)

            Result.success(workDataOf(KEY_IMAGE_URI to outputUri.toString()))
        } catch (throwable: Throwable) {
            Result.failure()
        }

    }
}