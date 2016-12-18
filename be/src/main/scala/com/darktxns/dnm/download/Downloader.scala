package com.darktxns.dnm.download

import java.io.File
import java.net.URL
import java.util.function.Supplier

import org.apache.commons.io.FileUtils


class Downloader(val source:DownloadLink) extends Supplier[File]
{
    override def get(): File =
    {
        val dest = new File( source.fileName )

        println(s"Dest: ${dest.getAbsolutePath}")

        FileUtils.copyURLToFile(new URL(source.fullUrl), dest)

        dest
    }
}
