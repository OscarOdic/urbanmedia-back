package utils

import models.{DetailsPlace, GeoLocPlace, Reaction}
import play.api.libs.json.{Format, Json}

object JsonFormatters {
  implicit val geoLocPlaceFormat: Format[GeoLocPlace] = Json.format[GeoLocPlace]
  implicit val reactionFormat: Format[Reaction] = Json.format[Reaction]
  implicit val detailsPlaceFormat: Format[DetailsPlace] = Json.format[DetailsPlace]
}
