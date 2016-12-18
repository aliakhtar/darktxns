package com.darktxns.dnm.download

import java.io.File
import java.net.URL
import java.util.function.Supplier

import org.apache.commons.io.FileUtils


class Downloader(val source:DownloadLink) extends Supplier[DownloadResult]
{
    override def get(): DownloadResult =
    {
        val dest = new File( source.fileName )

        println(s"Starting download: $source")
        FileUtils.copyURLToFile(new URL(source.fullUrl), dest)

        println(s"Finished downloading $source")
        DownloadResult(source, dest)
    }
}
