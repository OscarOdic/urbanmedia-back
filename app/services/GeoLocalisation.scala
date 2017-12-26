package services

import models.GeoLocPlace
import slick.jdbc.PostgresProfile.backend.DatabaseDef
import slick.jdbc.PostgresProfile.api._
import models.Tables.geoLocPlaces

import scala.concurrent.Future

object GeoLocalisation {
  def getGeoLocPlaces(northEast: (Double, Double), southWest: (Double, Double))(implicit db: DatabaseDef): Future[Seq[GeoLocPlace]] =
    db.run(geoLocPlaces.filter(gp =>
      gp.latitude < northEast._1 &&
        gp.latitude > southWest._1 &&
        gp.longitude < northEast._2 &&
        gp.longitude > southWest._2
    ).result)
}
