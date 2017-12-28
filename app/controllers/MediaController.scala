package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json.Json
import utils.SlickDatabase
import models.Tables._
import services.{AuthService, MediaService, UploadService}
import slick.jdbc.PostgresProfile.api._
import utils.JsonFormatters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MediaController @Inject() extends Controller {
  def getImage(placeid: Int, imageid: Int) = Action.async {
    val db = SlickDatabase.get
    db.run(images.filter(i => i.placeId === placeid && i.id === imageid).map(_.media).result.transactionally).map(_.headOption match {
      case Some(image) => Ok(image).as("image")
      case None => NotFound(s"Place with id $placeid or image with id $imageid not found")
    })
  }

  def getImages(placeid: Int) = Action.async {
    val db = SlickDatabase.get
    MediaService.getImagesFromPlace(placeid)(db).map(l => Ok(Json.toJson(l)))
  }

  def getSong(placeid: Int, songid: Int) = Action.async {
    val db = SlickDatabase.get
    db.run(songs.filter(i => i.placeId === placeid && i.id === songid).map(_.media).result.transactionally).map(_.headOption match {
      case Some(song) => Ok(song).as("audio")
      case None => NotFound(s"Place with id $placeid or song with id $songid not found")
    })
  }

  def getSongs(placeid: Int) = Action.async {
    val db = SlickDatabase.get
    MediaService.getSongsFromPlace(placeid)(db).map(l => Ok(Json.toJson(l)))
  }

  def getVideo(placeid: Int, videoid: Int) = Action.async {
    val db = SlickDatabase.get
    db.run(videos.filter(i => i.placeId === placeid && i.id === videoid).map(_.media).result.transactionally).map(_.headOption match {
      case Some(video) => Ok(video).as("video/mp4")
      case None => NotFound(s"Place with id $placeid or video with id $videoid not found")
    })
  }

  def getVideos(placeid: Int) = Action.async {
    val db = SlickDatabase.get
    MediaService.getVideosFromPlace(placeid)(db).map(l => Ok(Json.toJson(l)))
  }

  def uploadImage(placeid: Int) = Action.async(parse.temporaryFile) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = UploadService.uploadFile(images, placeid, request.body, account.userName)(db)
      db.run(insert).map(_ => Ok("added"))
    }
  }

  def uploadSong(placeid: Int) = Action.async(parse.temporaryFile) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = UploadService.uploadFile(songs, placeid, request.body, account.userName)(db)
      db.run(insert).map(_ => Ok("added"))
    }
  }

  def uploadVideo(placeid: Int) = Action.async(parse.temporaryFile) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = UploadService.uploadFile(videos, placeid, request.body, account.userName)(db)
      db.run(insert).map(_ => Ok("added"))
    }
  }

  def deleteImage(imageid: Int) = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val query = images.filter(_.id === imageid)
      db.run(query.map(_.author).result).flatMap(_.head match {
        case author if author == account.userName => db.run(query.delete).map(_ => Ok("deleted"))
        case _ => Future(Unauthorized(s"${account.userName} is unauthorized to delete this image"))
      })
    }
  }

  def deleteSong(songid: Int) = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val query = songs.filter(_.id === songid)
      db.run(query.map(_.author).result).flatMap(_.head match {
        case author if author == account.userName => db.run(query.delete).map(_ => Ok("deleted"))
        case _ => Future(Unauthorized(s"${account.userName} is unauthorized to delete this song"))
      })
    }
  }

  def deleteVideo(videoid: Int) = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val query = videos.filter(_.id === videoid)
      db.run(query.map(_.author).result).flatMap(_.head match {
        case author if author == account.userName => db.run(query.delete).map(_ => Ok("deleted"))
        case _ => Future(Unauthorized(s"${account.userName} is unauthorized to delete this video"))
      })
    }
  }
}
