package controllers

import javax.inject._

import models.Tables.geoLocPlaces
import slick.jdbc.PostgresProfile.api._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json._
import utils.SlickDatabase

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  def convertToJson(values: Seq[Double]): JsValue = Json.toJson(values)
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action.async {
    import utils.JsonFormatters._
    val db = SlickDatabase.get
    db.run(geoLocPlaces.result)
      .map(g => Ok(Json.toJson(g)))
  }
}
