package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json._
import services.GeoLocalisationService._
import utils.SlickDatabase

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class GeoLocController @Inject() extends Controller {

  def convertToJson(values: Seq[Double]): JsValue = Json.toJson(values)

  def index(northeast: String, southwest: String) = Action.async {
    import utils.JsonFormatters._
    val db = SlickDatabase.get
    val ne = northeast.split(",").toList.map(_.toDouble) match {
      case List(latitude, longitude) => (latitude, longitude)
    }
    val sw = southwest.split(",").toList.map(_.toDouble) match {
      case List(latitude, longitude) => (latitude, longitude)
    }
    getGeoLocPlaces(ne, sw)(db).map(result => Ok(Json.toJson(result)))
  }
}
