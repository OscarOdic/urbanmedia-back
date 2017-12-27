package utils

import models._
import play.api.libs.json.{Format, Json}

object JsonFormatters {
  implicit val geoLocPlaceFormat: Format[GeoLocPlace] = Json.format[GeoLocPlace]
  implicit val reactionFormat: Format[Reaction] = Json.format[Reaction]
  implicit val detailsPlaceFormat: Format[DetailsPlace] = Json.format[DetailsPlace]
  implicit val accountFormat: Format[Account] = Json.format[Account]
  implicit val userFormat: Format[User] = Json.format[User]
}
