package utils

import slick.jdbc.PostgresProfile.api._
import slick.jdbc.PostgresProfile.backend.DatabaseDef

object SlickDatabase {
  val USERNAME = "postgres"
  val PASSWORD = "postgres"
  val DATABASE = "urbanmedia"
  val URL = s"jdbc:postgresql://localhost:5432/$DATABASE?user=$USERNAME&password=$PASSWORD"
  val DRIVER = "org.postgresql.Driver"

  def get: DatabaseDef = Database.forURL(URL, driver = DRIVER)
}
