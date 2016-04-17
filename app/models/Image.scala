package models

import java.io.File

/**
  * Created by jaime on 4/16/16.
  */
case class Image(id: Option[Long], filename: String, binaryData: File)
