package models

import java.sql.Date

/**
  * Created by jaime on 4/16/16.
  */

trait ExpenseType

case class Gas() extends ExpenseType
case class Repair() extends ExpenseType
case class Upgrade() extends ExpenseType
case class CarWash() extends ExpenseType
case class Insurance() extends ExpenseType
case class Maintenance() extends ExpenseType
case class Miscellaneous() extends ExpenseType


case class Expense(id: Option[Long], expenseType: ExpenseType, expenseDate: Date, description: String,
                   quantity: Option[Double], price: Double, imageId: Long, creationDate: Option[Date])
