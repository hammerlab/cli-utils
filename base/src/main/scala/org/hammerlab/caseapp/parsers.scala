package org.hammerlab.caseapp

import caseapp.core.Error
import caseapp.core.argparser.{ ArgParser, SimpleArgParser }
import cats.syntax.either._

import scala.collection.generic.CanBuildFrom
import scala.reflect.ClassTag

trait parsers {
  implicit def arrParser[T: ClassTag](implicit p: ArgParser[T]): ArgParser[Array[T]] =
    SimpleArgParser.from("elements") {
      _
        .split(",")
        .map {
          p(None, _)
        }
        .foldLeft[
          Either[
            Error,
            Vector[T]
          ]
        ](
          Right(Vector())
        ) {
          case ( Left(error),  Left( cur )) ⇒  Left(error.append(cur))
          case (          _ ,  Left(error)) ⇒  Left(error)
          case ( Left(error),           _ ) ⇒  Left(error)
          case (Right(ranges), Right(range)) ⇒ Right(ranges :+ range)
        }
        .map(
          _.toArray
        )
    }

  implicit def seqParser[T: ClassTag, I[_] <: Iterable[_]](implicit
                                                           p: ArgParser[Array[T]],
                                                           ev: I[T] <:< Iterable[T],
                                                           cbf: CanBuildFrom[Array[T], T, I[T]]): ArgParser[I[T]] =
    SimpleArgParser.from("elements") {
      p(
        None,
        _
      )
      .map(
        cbf(_)
          .result()
      )
    }
}
