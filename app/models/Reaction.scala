package models

case class Reaction(
                     id: Option[Int] = None,
                     placeId: Option[Int] = None,
                     user: String,
                     message: String
                   )