package com.darktxns.dnm

import java.io.File
import java.net.URL
import java.util.concurrent.Callable

import org.apache.commons.io.FileUtils


class Downloader(val source:DownloadLink)  extends Callable[File]
{
    override def call(): File =
    {
        //Download to the filename of the source in the current working directory where this is being run.

        val dest = new File( source.fileName )

        println(s"Dest: ${dest.getAbsolutePath}")

        FileUtils.copyURLToFile(new URL(source.fullUrl), dest)

        dest
    }
}
