package models

case class User(
                 userName: String,
                 bio: Option[String],
                 follow: List[Place]
               )
