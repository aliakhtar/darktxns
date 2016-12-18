package com.darktxns.dnm.download

import java.io.File

import com.amazonaws.event.ProgressEvent
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.internal.S3ProgressListener
import com.amazonaws.services.s3.transfer.{PersistableTransfer, TransferManager, Upload}
import com.darktxns.Environment

class S3Uploader(private val env: Environment)
{
    private val transferer = new TransferManager( env.awsCreds )

    def upload(file:File):Upload =
    {
        val listener = new S3ProgressListener
        {
            override def onPersistableTransfer(persistableTransfer: PersistableTransfer):Unit =
            {
                println(s"PersistableTransfer: ${file.getName}, $persistableTransfer")
            }

            override def progressChanged(progressEvent: ProgressEvent):Unit =
            {
                println(s"Progress: ${file.getName}, $progressEvent")
            }
        }

        val request = new PutObjectRequest(env.config.dataBucket, file.getName, file)
        transferer.upload(request, listener)
    }
}
