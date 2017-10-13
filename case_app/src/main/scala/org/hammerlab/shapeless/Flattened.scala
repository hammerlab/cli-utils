package org.hammerlab.shapeless

import shapeless._
import shapeless.ops.hlist.Prepend

trait FlattenedI {

  type Gen[T, L <: HList]
  def genTo[T, L <: HList](t: T)(implicit g: Gen[T, L]): L

  trait Flattened[L] extends Serializable {
    type F <: HList
    def apply(l: L): F
  }

  trait LowestPri {
    type Aux[L, F0] = Flattened[L] { type F = F0 }

    def make[L, F0 <: HList](fn: L ⇒ F0): Aux[L, F0] =
      new Flattened[L] {
        type F = F0
        override def apply(l: L) = fn(l)
      }

    // Put anything with HNil, if nothing else matches
    implicit def directSingle[H]: Aux[H, H :: HNil] = make(_ :: HNil)
  }

  trait LowPriFlattenedImplicits extends LowestPri {
    // prepend an element directly if it can't be flattened further (via higher-priority implicits below)
    implicit def directCons[H, T <: HList, FT <: HList](implicit flatT: Aux[T, FT]): Aux[H :: T, H :: FT] =
      make(
        l ⇒
          l.head ::
            flatT(l.tail)
      )
  }

  object Flattened extends LowPriFlattenedImplicits {

    def apply[L](implicit flat: Flattened[L]): Aux[L, flat.F] = flat

    implicit val hnil: Aux[HNil, HNil] = make(l ⇒ l)

    // Flatten and prepend an HList
    implicit def nestedCons[H <: HList, FH <: HList, T <: HList, FT <: HList, Out <: HList](
        implicit
        flatH: Aux[H, FH],
        flatT: Aux[T, FT],
        concat: Prepend.Aux[FH, FT, Out]): Aux[H :: T, Out] =
      make(
        l ⇒
          concat(
            flatH(l.head),
            flatT(l.tail)
          )
      )

    // Flatten and prepend a Product (e.g. case-class)
    implicit def nestedCCCons[H <: Product, HL <: HList, FH <: HList, T <: HList, FT <: HList, Out <: HList](
        implicit
        gen: Gen[H, HL],
        flatH: Aux[HL, FH],
        flatT: Aux[T, FT],
        concat: Prepend.Aux[FH, FT, Out]): Aux[H :: T, Out] =
      make(
        l ⇒
          concat(
            flatH(genTo(l.head)),
            flatT(l.tail)
          )
      )

    // Flatten a case-class directly
    implicit def cc[CC <: Product, L <: HList, FL <: HList](implicit gen: Gen[CC, L], flat: Aux[L, FL]): Aux[CC, FL] =
      make(
        cc ⇒
          flat(
            genTo(cc)
          )
      )
  }

}

object FlattenedHList extends FlattenedI {
  override type Gen[T, L] = Generic.Aux[T, L]
  override def genTo[T, L](t: T)(implicit g: Gen[T, L]) = g.to(t)
}

object FlattenedLabelledHList extends FlattenedI {
  override type Gen[T, L] = LabelledGeneric.Aux[T, L]
  override def genTo[T, L](t: T)(implicit g: Gen[T, L]) = g.to(t)
}
