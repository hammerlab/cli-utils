package org.hammerlab.shapeless

import org.hammerlab.test.Suite
import shapeless._

class FlattenedTest
  extends Suite {

  import FindTest._

  test("hlist summons") {
    import org.hammerlab.shapeless.FlattenedHList._

    implicitly[Flattened.Aux[Int :: HNil, Int :: HNil]]

    implicitly[Flattened.Aux[A, Int :: HNil]]
    implicitly[Flattened.Aux[B, String :: HNil]]

    implicitly[Flattened.Aux[B :: HNil, String :: HNil]]

    implicitly[Flattened[Int :: String :: HNil]]

    implicitly[Flattened.Aux[
      A :: String :: HNil,
      Int :: String :: HNil
    ]]
    implicitly[Flattened.Aux[
      Int :: B :: HNil,
      Int :: String :: HNil
    ]]

    implicitly[Flattened.Aux[
      (Int :: HNil) :: String :: HNil,
      Int :: String :: HNil
    ]]
    implicitly[Flattened.Aux[
      Int :: (String :: HNil) :: HNil,
      Int :: String :: HNil
    ]]

    implicitly[Flattened.Aux[
      (Int :: HNil) :: HNil,
      Int :: HNil
    ]]
    implicitly[Flattened.Aux[
      (String :: HNil) :: HNil,
      String :: HNil
    ]]

    Flattened.nestedCons[
      Int :: HNil,
      Int :: HNil,
      String :: HNil,
      String :: HNil,
      Int :: String :: HNil
    ]
    implicitly[Flattened.Aux[
      (Int :: HNil) :: (String :: HNil) :: HNil,
      Int :: String :: HNil
    ]]

    implicitly[Flattened[A :: String :: HNil]]
    implicitly[Flattened[A :: B :: HNil]]

    implicitly[Flattened[(Int :: HNil) :: (String :: HNil) :: HNil]]

    implicitly[Flattened.Aux[A :: B :: HNil, Int :: String :: HNil]]
    implicitly[Flattened.Aux[C, Int :: String :: HNil]]
    implicitly[Flattened.Aux[F, Int :: String :: Boolean :: HNil]]
    implicitly[Flattened.Aux[String, String :: HNil]]
  }

/*
  test("labelled summons") {
    import FlattenedLabelledHList._

    implicitly[Flattened[C]]
    Flattened[C, Int :: String :: HNil]
    Flattened[F, Int :: String :: Boolean :: HNil]
  }
*/
}
