package controllers

import javax.inject._

import daos.{ExpensesDAO, ImagesDAO}
import models.Image
import play.api._
import play.api.mvc._

/**
  * Created by jaime on 4/13/16.
  */
@Singleton
class ExpenseController @Inject() (expensesDAO: ExpensesDAO, imagesDAO: ImagesDAO)
  extends Controller {

  def index = TODO // TODO: show list of expenses

  def form = Action {
    Ok(views.html.expenses.form())
  }

  def save = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      val tmpFile = new File(filename)
      imagesDAO.save(Image(None, filename, picture.ref.moveTo(tmpFile)))
      tmpFile.delete


      Ok("Expense registered OK")
    }.getOrElse {
      Redirect(routes.HomeController.index).flashing(
        "error" -> "Missing file")
    }
  }

}
