package org.hammerlab.cli.app

import shapeless._

trait Select[L, +U] extends Serializable {
  def apply(l: L): U
}

object Select {
  def apply[L, U](fn: L ⇒ U) = new Select[L, U] { override def apply(l: L) = fn(l) }

  implicit def cc[T, CC, L <: HList](implicit
                                     gen: Generic.Aux[CC, L],
                                     select: Select[L, T]): Select[CC, T] =
    Select[CC, T](cc ⇒ select(gen.to(cc)))

  implicit def select[H, T <: HList]: Select[H :: T, H] =
    Select[H :: T, H](_.head)

  implicit def recurse[H, L <: HList, U](implicit st : Select[L, U]): Select[H :: L, U] =
    Select[H :: L, U](l ⇒ st(l.tail))
}
