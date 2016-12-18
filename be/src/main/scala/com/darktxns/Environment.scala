package com.darktxns

import com.amazonaws.auth.BasicAWSCredentials
import com.darktxns.io.Reader
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods

object Environment
{
    def build():Environment =
    {
        val json = Reader.readResource("config.json")

        implicit val formats = DefaultFormats

        val config = JsonMethods.parse(json).extract[Config]
        new Environment(config)
    }
}

class Environment(val config: Config)
{
    val awsCreds = new BasicAWSCredentials(config.awsAccessKey, config.secretKey)


    override def toString = s"Environment($awsCreds, $config)"
}
