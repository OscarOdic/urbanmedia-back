package utils

import models.GeoLocPlace
import play.api.libs.json.{Format, Json}

object JsonFormatters {
  implicit val geoLocPlaceFormat: Format[GeoLocPlace] = Json.format[GeoLocPlace]
}
