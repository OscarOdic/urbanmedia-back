package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json._

@Singleton
class HomeController @Inject() extends Controller {

  def convertToJson(values: Seq[Double]): JsValue = Json.toJson(values)

  def index = Action {
    Ok("Hello world !")
  }
}
