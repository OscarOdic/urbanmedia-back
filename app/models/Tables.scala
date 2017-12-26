package models

import slick.jdbc.PostgresProfile.api._

object Tables extends App {
  class GeoLocPlaces(tag: Tag) extends Table[GeoLocPlace](tag, "geolocplace") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def latitude = column[Double]("latitude")
    def longitude = column[Double]("longitude")
    def * = (id, name, latitude, longitude) <> (GeoLocPlace.tupled, GeoLocPlace.unapply)
  }
  def geoLocPlaces = TableQuery[GeoLocPlaces]

  class Image(tag: Tag) extends Table[(Int, Int, Array[Byte])](tag, "image") {
    def id = column[Int]("id", O.PrimaryKey)
    def placeId = column[Int]("placeid")
    def image = column[Array[Byte]]("image")
    def * = (id, placeId, image)
    def place = foreignKey("imageplace_fk", placeId, geoLocPlaces)(_.id)
  }
  def images = TableQuery[Image]

  class Reactions(table: String)(tag: Tag) extends Table[Reaction](tag, table) {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def placeId = column[Int]("placeid")
    def user = column[String]("username")
    def message = column[String]("message")
    def * = (user, message) <> (Reaction.tupled, Reaction.unapply)
    def place = foreignKey(s"${table}place_fk", placeId, geoLocPlaces)(_.id)
  }

  class Comments(tag: Tag) extends Reactions("comment")(tag)
  def comments = TableQuery[Comments]

  class Warnings(tag: Tag) extends Reactions("warning")(tag)
  def warnings = TableQuery[Warnings]
}