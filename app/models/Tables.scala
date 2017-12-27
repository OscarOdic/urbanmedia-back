package models

import slick.jdbc.PostgresProfile.api._

object Tables extends App {
  class GeoLocPlaces(tag: Tag) extends Table[GeoLocPlace](tag, "geolocplace") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def latitude = column[Double]("latitude")
    def longitude = column[Double]("longitude")
    def whoAdded = column[String]("whoadded")
    def * = (id.?, name, latitude, longitude, whoAdded) <> (GeoLocPlace.tupled, GeoLocPlace.unapply)
  }
  def geoLocPlaces = TableQuery[GeoLocPlaces]

  class Medias(table: String)(tag: Tag) extends Table[(Option[Int], Int, Array[Byte], String)](tag, table) {
    def id = column[Int]("id", O.PrimaryKey)
    def placeId = column[Int]("placeid")
    def media = column[Array[Byte]]("media")
    def author = column[String]("author")
    def * = (id.?, placeId, media, author)
    def place = foreignKey(s"${table}place_fk", placeId, geoLocPlaces)(_.id)
  }

  class Images(tag: Tag) extends Medias("image")(tag)
  def images = TableQuery[Images]

  class Songs(tag: Tag) extends Medias("song")(tag)
  def songs = TableQuery[Songs]

  class Videos(tag: Tag) extends Medias("video")(tag)
  def videos = TableQuery[Videos]

  class Reactions(table: String)(tag: Tag) extends Table[Reaction](tag, table) {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def placeId = column[Int]("placeid")
    def user = column[String]("username")
    def message = column[String]("message")
    def * = (id.?, placeId.?, user, message) <> (Reaction.tupled, Reaction.unapply)
    def place = foreignKey(s"${table}place_fk", placeId, geoLocPlaces)(_.id)
  }

  class Comments(tag: Tag) extends Reactions("comment")(tag)
  def comments = TableQuery[Comments]

  class Warnings(tag: Tag) extends Reactions("warning")(tag)
  def warnings = TableQuery[Warnings]

  class Accounts(tag: Tag) extends Table[Account](tag, "account") {
    def userName = column[String]("username", O.PrimaryKey)
    def password = column[String]("password")
    def bio = column[String]("bio")
    def * = (userName, password, bio.?) <> (Account.tupled, Account.unapply)
  }
  def accounts = TableQuery[Accounts]

  class Follow(tag: Tag) extends Table[(String, Int)](tag, "follow") {
    def userName = column[String]("username")
    def placeId = column[Int]("placeid")
    def * = (userName, placeId)
    def account = foreignKey(s"followaccount_fk", userName, accounts)(_.userName)
    def place = foreignKey(s"followplace_fk", placeId, geoLocPlaces)(_.id)
  }
  def follow = TableQuery[Follow]
}