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

      imagesDAO.save(Image(None, picture.filename, picture.ref.file))

      Ok("Expense registered OK")
    }.getOrElse {
      Redirect(routes.HomeController.index).flashing(
        "error" -> "Missing file")
    }
  }

}
