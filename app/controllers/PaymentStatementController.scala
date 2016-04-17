package controllers

import javax.inject._

import play.api.mvc._
import play.api.db._

/**
  * Created by jaime on 4/13/16.
  */
@Singleton
class PaymentStatementController @Inject() (db: Database) extends Controller {

  def index = TODO // TODO: show payments list

  def upload = TODO // TODO: upload payment statement

  def trips = TODO // TODO: show

}
