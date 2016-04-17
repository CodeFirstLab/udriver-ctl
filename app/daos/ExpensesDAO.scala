package daos

import java.sql.{Date, Types}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject._

import models._
import play.api.db._

import scala.collection.mutable.ListBuffer

/**
  * Created by jaime on 4/16/16.
  */
@Singleton
class ExpensesDAO @Inject() (db: Database) {

  private val INSERT = "INSERT INTO expenses (expense_type, expense_date, description, quantity, price, image_id) VALUES (?, ?, ?, ?, ?)"
  private val FIND_BY_ID = "SELECT id, expense_type, expense_date, description, quantity, price, image_id, creation_date FROM expenses WHERE id = ?"
  private val FIND_BY_DATE = "SELECT id, expense_type, expense_date, description, quantity, price, image_id, creation_date FROM expenses WHERE expense_date BETWEEN ? AND ?"

  def save(expense: Expense) = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement(INSERT)
      stmt.setString(1, expense.expenseType.toString)
      stmt.setDate(2, expense.expenseDate)
      stmt.setString(3, expense.description)
      if (expense.quantity.nonEmpty)
        stmt.setDouble(4, expense.quantity.get)
      else
        stmt.setNull(4, Types.NUMERIC)
      stmt.setDouble(5, expense.price)
      stmt.setLong(6, expense.imageId)
      stmt.execute
    }
  }

  def findById(id: Long): Option[Expense] = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement(FIND_BY_ID)
      stmt.setLong(1, id)
      val rs = stmt.executeQuery
      if (rs.next) {
        val expenseType = rs.getString("expense_type") match {
          case "Gas()" => Gas()
          case "Repair()" => Repair()
          case "Upgrade()" => Upgrade()
          case "CarWash()" => CarWash()
          case "Insurance()" => Insurance()
          case "Maintenance()" => Maintenance()
          case "Miscellaneous()" =>  Miscellaneous()
        }

        Some(Expense(Some(rs.getLong("id")), expenseType, rs.getDate("expense_date"),
          rs.getString("description"), Option(rs.getDouble("quantity")), rs.getDouble("price"),
          rs.getLong("image_id"), Option(rs.getDate("creation_date"))))
      } else None
    }
  }

  def findByDate(startDate: LocalDateTime, endDate: LocalDateTime): List[Expense] = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement(FIND_BY_DATE)
      val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      stmt.setDate(1, Date.valueOf(dtf.format(startDate)))
      stmt.setDate(2, Date.valueOf(dtf.format(endDate)))
      val rs = stmt.executeQuery

      val expenses = ListBuffer[Expense]()

      while (rs.next) {
        val expenseType = rs.getString("expense_type") match {
          case "Gas()" => Gas()
          case "Repair()" => Repair()
          case "Upgrade()" => Upgrade()
          case "CarWash()" => CarWash()
          case "Insurance()" => Insurance()
          case "Maintenance()" => Maintenance()
          case "Miscellaneous()" =>  Miscellaneous()
        }

        expenses += Expense(Some(rs.getLong("id")), expenseType, rs.getDate("expense_date"),
          rs.getString("description"), Option(rs.getDouble("quantity")), rs.getDouble("price"),
          rs.getLong("image_id"), Option(rs.getDate("creation_date")))
      }
      expenses.toList
    }
  }
}
