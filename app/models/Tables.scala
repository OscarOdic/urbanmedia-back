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
}