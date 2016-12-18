package com.darktxns.dnm

import java.io.File

package object download
{
    case class DownloadResult(link:DownloadLink, file: File)
}
