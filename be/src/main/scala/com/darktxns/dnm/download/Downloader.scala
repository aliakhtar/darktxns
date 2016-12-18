package com.darktxns.dnm.download

import java.io.File
import java.net.URL
import java.util.function.Supplier

import org.apache.commons.io.FileUtils


class Downloader(val source:DownloadLink) extends Supplier[DownloadResult]
{
    override def get(): DownloadResult =
    {
        new File("downloads").mkdirs()

        val dest = new File(  s"downloads/${source.fileName}" )

        println(s"Starting download: ${source.fullUrl}")
        FileUtils.copyURLToFile(new URL(source.fullUrl), dest)

        println(s"Finished downloading ${source.fullUrl}")
        DownloadResult(source, dest)
    }
}
