import java.sql.Date
import java.time.{LocalDateTime, Month}
import java.util.Calendar

import daos._
import models._
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api._
import play.api.inject.guice.GuiceApplicationBuilder

/**
  * Created by jaime on 4/16/16.
  */
class ExpensesDAOSpec extends PlaySpec with OneAppPerTest {

  val application = new GuiceApplicationBuilder()
    .loadConfig(env => Configuration.load(env))
    .build

  "ExpensesDAO" should {
    "be able to save many expenses to database" in {
      val expensesDAO = application.injector.instanceOf[ExpensesDAO]
      expensesDAO.save(Expense(None, Miscellaneous(), new Date(Calendar.getInstance.getTimeInMillis),
        "Laminado 3M y Alarma para el carro.", None, 9000, 1, None))
      expensesDAO.save(Expense(None, Gas(), new Date(Calendar.getInstance.getTimeInMillis),
        "Combustible.", Some(11.13), 946.05, 1, None))
    }

    "be able to find expense by id" in {
      val expensesDAO = application.injector.instanceOf[ExpensesDAO]
      val expense = expensesDAO.findById(2L)
      assert(expense.nonEmpty)
    }

    "be able to find expenses by date range" in {
      val expensesDAO = application.injector.instanceOf[ExpensesDAO]
      val expense = expensesDAO.findByDate(LocalDateTime.of(2016, Month.APRIL, 1, 0, 0), LocalDateTime.now())
      assert(expense.nonEmpty)
    }
  }

}
