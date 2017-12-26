package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json._
import utils.SlickDatabase
import services.PlaceService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PlaceController @Inject() extends Controller {

  def convertToJson(values: Seq[Double]): JsValue = Json.toJson(values)

  def index(id: Int) = Action.async {
    import utils.JsonFormatters._
    val db = SlickDatabase.get
    PlaceService.getPlace(id)(db).map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound(s"Place with id $id not found")
    }
  }
}
