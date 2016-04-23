import java.io.{File, FileWriter}

import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api._
import play.api.inject.guice.GuiceApplicationBuilder
import daos._
import models._

/**
  * Created by jaime on 4/16/16.
  */
class ImagesDAOSpec extends PlaySpec with OneAppPerTest {

  val application = new GuiceApplicationBuilder()
    .loadConfig(env => Configuration.load(env))
    .build

  "ImagesDAO" should {
    "save image from file" in {
      val imageFile = new File("test/IMG_5745.JPG")
      val imagesDAO = application.injector.instanceOf[ImagesDAO]
      println(imagesDAO.save(Image(None, imageFile.getName, imageFile)))
    }

    "find image by id" in {
      val imagesDAO = application.injector.instanceOf[ImagesDAO]
      val image = imagesDAO.findById(6L)
      if (image.nonEmpty) {
        image.get.binaryData.createNewFile
      }
    }
  }

}
