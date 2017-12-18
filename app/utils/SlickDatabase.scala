package utils

import slick.jdbc.PostgresProfile.api._

object SlickDatabase {
  val USERNAME = "postgres"
  val PASSWORD = "postgres"
  val DATABASE = "urbanmedia"
  val URL = s"jdbc:postgresql://localhost/$DATABASE?user=$USERNAME&password=$PASSWORD"
  val DRIVER = "org.postgresql.Driver"

  def get = Database.forURL(URL, driver = DRIVER)
}
