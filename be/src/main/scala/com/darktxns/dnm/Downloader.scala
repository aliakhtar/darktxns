package com.darktxns.dnm

import java.io.File
import java.util.concurrent.Callable


class Downloader(val source:DownloadLink)  extends Callable[File]
{
    override def call(): File =
    {
        null
    }
}
