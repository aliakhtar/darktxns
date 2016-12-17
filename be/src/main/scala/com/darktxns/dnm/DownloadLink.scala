package com.darktxns.dnm

import com.darktxns.dnm.DatasetType.DatasetType

object DownloadLink
{
    /* The parsed links are just the file names, they are appended to the below dir to get the full download URL */

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
        if (fileName == "2015-sr2doug-claimedsr2leaks.tar.xz") //This dataset is named inconsistently from others,
            return "SilkRoad2"                                 // hence needs its own exception

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

    /**
      * If the filename contains -forum , consider it a forum dataset, otherwise, market
      */
    private def parseType(fileName:String):DatasetType =
    {
        if (! fileName.toLowerCase().contains("forum"))
            DatasetType.Market
        else
            DatasetType.Forum

    }


    override def toString = s"DownloadLink($fullUrl, $marketName, $guessedType, $fileName)"
}
