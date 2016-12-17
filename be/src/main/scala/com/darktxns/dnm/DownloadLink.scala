package com.darktxns.dnm

import com.darktxns.dnm.DatasetType.DatasetType

object DownloadLink
{
    val DOWNLOAD_DIR = "https://archive.org/download/dnmarchives/"
    val EXTENSION = ".tar.xz"
}

class DownloadLink(val fileName: String)
{
    val fullUrl:String = DownloadLink.DOWNLOAD_DIR + fileName
    val marketName:String = parseMarketName(fileName)
    val guessedType:DatasetType = parseType(fileName)


    private def parseMarketName(fileName:String):String =
    {
        if (fileName == "2015-sr2doug-claimedsr2leaks.tar.xz")
            return "SilkRoad2"

        val processed = fileName
            .trim
            .substring(0, fileName.length - DownloadLink.EXTENSION.length) //Parse out the extension
            .toLowerCase
            .replace("market", " Market")
            .capitalize

        if (! processed.contains("-"))
            return processed

        processed.splitAt( processed.indexOf("-") )._1
    }

    private def parseType(fileName:String):DatasetType =
    {
        if (! fileName.toLowerCase().contains("forum"))
            DatasetType.Market
        else
            DatasetType.Forum

    }


    override def toString = s"DownloadLink($fullUrl, $marketName, $guessedType, $fileName)"
}
