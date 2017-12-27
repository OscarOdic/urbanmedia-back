package models

case class Account(
                    userName: String,
                    password: String,
                    bio: Option[String] = None
                  )

case class User(
                 userName: String,
                 bio: Option[String] = None,
                 follow: List[GeoLocPlace] = List.empty
               )
