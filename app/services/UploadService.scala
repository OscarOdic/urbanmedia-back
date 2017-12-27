package services

import java.nio.file.Files

import models.Tables.Medias
import play.api.libs.Files.TemporaryFile
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.PostgresProfile.backend.DatabaseDef

object UploadService {
  def uploadFile[T <: Medias](table : TableQuery[T], id: Int, body: TemporaryFile, author: String)(db: DatabaseDef) =
    DBIO.seq(
      table.map(t => (t.placeId, t.media, t.author)) += (id, Files.readAllBytes(body.file.toPath), author)
    ).transactionally
}
