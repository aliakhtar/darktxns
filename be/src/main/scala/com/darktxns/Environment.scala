package com.darktxns

import com.amazonaws.auth.{AWSCredentials, BasicAWSCredentials}
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

        val creds = new BasicAWSCredentials(config.awsAccessKey, config.secretKey)
        new Environment(creds)
    }
}

class Environment(val awsCreds:AWSCredentials)
{

}
