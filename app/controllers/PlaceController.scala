package controllers

import javax.inject._

import models.GeoLocPlace
import play.api.mvc._
import play.api.libs.json.Json
import utils.SlickDatabase
import models.Tables._
import services.{PlaceService, UploadService}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PlaceController @Inject() extends Controller {

  def index(id: Int) = Action.async {
    import utils.JsonFormatters._
    val db = SlickDatabase.get
    PlaceService.getPlace(id)(db).map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound(s"Place with id $id not found")
    }
  }

  def randomBackgroundImage(id: Int) = Action.async {
    val db = SlickDatabase.get
    val rand = SimpleFunction.nullary[Double]("random")
    db.run(images.filter(_.placeId === id).sortBy(_ => rand).take(1).map(_.media).result.transactionally).map(_.headOption match {
      case Some(image) => Ok(image).as("image/png")
      case None => NotFound(s"Place with id $id not found")
    })
  }

  def add = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    val insert = DBIO.seq(
      geoLocPlaces += GeoLocPlace(
        None,
        request.body("name").head,
        request.body("latitude").head.toDouble,
        request.body("longitude").head.toDouble
      )
    )
    db.run(insert).map(_ => Ok("added"))
  }

  def uploadImage(id: Int) = Action.async(parse.temporaryFile) { request =>
    val db = SlickDatabase.get
    val insert = UploadService.uploadFile(images, id, request.body)(db)
    db.run(insert).map(_ => Ok("added"))
  }

  def uploadSong(id: Int) = Action.async(parse.temporaryFile) { request =>
    val db = SlickDatabase.get
    val insert = UploadService.uploadFile(songs, id, request.body)(db)
    db.run(insert).map(_ => Ok("added"))
  }

  def uploadVideo(id: Int) = Action.async(parse.temporaryFile) { request =>
    val db = SlickDatabase.get
    val insert = UploadService.uploadFile(videos, id, request.body)(db)
    db.run(insert).map(_ => Ok("added"))
  }
}
