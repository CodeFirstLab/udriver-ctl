package daos

import java.io._
import java.util.UUID
import javax.inject.{Inject, Singleton}

import models.Image
import play.api.db.Database

/**
  * Created by jaime on 4/16/16.
  */
@Singleton
class ImagesDAO @Inject() (db: Database) {

  private val SELECT_IMAGE = "SELECT id, filename, binary_data, creation_date FROM images WHERE id = ?"
  private val INSERT_IMAGE = "INSERT INTO images (filename, binary_data) VALUES (?, ?) RETURNING id"

  def save(image: Image): Option[Long] = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement(INSERT_IMAGE)
      val data = fileToByteArray(image.binaryData)
      val bais = new ByteArrayInputStream(data)
      stmt.setString(1, image.filename)
      stmt.setBinaryStream(2, bais, data.length)
      val rs = stmt.executeQuery
      if (rs.next)
        Some(rs.getLong(1))
      else None
    }
  }

  def findById(id: Long): Option[Image] = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement(SELECT_IMAGE)
      stmt.setLong(1, id)
      val rs = stmt.executeQuery
      if (rs.next) {
        Some(Image(
          Some(rs.getLong("id")),
          rs.getString("filename"),
          byteArrayToFile(
            rs.getBytes("binary_data"),
            rs.getString("filename"))
        ))
      } else None
    }
  }

  private def byteArrayToFile(data: Array[Byte], fileName: String): File = {
    val file = new File(fileName)
    val fos = new FileOutputStream(file)
    val bais = new ByteArrayInputStream(data)
    val buffer = Array[Byte](4096.toByte)
    var size = bais.read(buffer)
    while (size != -1) {
      fos.write(buffer, 0, size)
      size = bais.read(buffer)
    }
    file
  }

  private def fileToByteArray(file: File): Array[Byte] = {
    val in = new FileInputStream(file)
    val byteOut = new ByteArrayOutputStream(file.length.toInt)
    val buffer = Array[Byte](4096.toByte)
    var size = in.read(buffer)
    while (size != -1) {
      byteOut.write(buffer, 0, size)
      size = in.read(buffer)
    }
    byteOut.toByteArray
  }
}
