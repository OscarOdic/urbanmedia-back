package services

import slick.jdbc.PostgresProfile.backend.DatabaseDef
import slick.jdbc.PostgresProfile.api._
import models.DetailsPlace
import models.Tables._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PlaceService {
  def getPlace(id: Int)(db: DatabaseDef): Future[Option[DetailsPlace]] = {
    db.run(geoLocPlaces.filter(_.id === id).result).flatMap(_.headOption match {
      case Some(place) => for {
        commentsPlace <- db.run(comments.filter(_.placeId === place.id).result)
        warningsPlace <- db.run(warnings.filter(_.placeId === place.id).result)
      } yield Some(DetailsPlace(
        place.id,
        place.name,
        commentsPlace.toList,
        warningsPlace.toList
      ))
      case None => Future(None)
    })
  }
}
