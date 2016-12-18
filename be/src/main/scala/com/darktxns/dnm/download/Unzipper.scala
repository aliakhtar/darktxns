package com.darktxns.dnm.download

import java.io.File
import java.lang.Runtime.getRuntime


class Unzipper extends Function1[File, File]
{
    override def apply(source: File): File =
    {
        val archiveName = source.getName.substring(0, source.getName.lastIndexOf( DownloadLink.EXTENSION ))
        val dest = new File( s"downloads/$archiveName" )

        dest.mkdirs()

        println(s"Unzipping ${source.getAbsolutePath} to ${dest.getAbsolutePath}")

        getRuntime.exec(s"tar -xf ${source.getAbsolutePath} -C ${dest.getAbsolutePath} ")

        dest
    }
}
