package org.hammerlab.shapeless

import org.hammerlab.test.Suite
import shapeless._
import shapeless.ops.record.Selector

class FindTest
  extends Suite {

  import FindTest._

  val lga = LabelledGeneric[A]
  val lgb = LabelledGeneric[B]
  val lgc = LabelledGeneric[C]

  val aa = A(123)
  val b = B("abc")
  val c = C(aa, b)

  Selector[lga.Repr, Witness.`'n`.T]
  Selector[lgb.Repr, Witness.`'s`.T]

  implicitly[Find[A, Witness.`'n`.T]]
  implicitly[Find.Aux[A, Witness.`'n`.T, Int]]


  Find[B, Witness.`'s`.T]

  Find[C, Witness.`'a`.T]

  Find[lgc.Repr, Witness.`'b`.T]
  Find[C, Witness.`'b`.T]

  Find[C, Witness.`'n`.T]
  Find[C, Witness.`'s`.T]

  test("summon") {
    Selector[lga.Repr, Witness.`'n`.T].apply(lga.to(aa)) should be(aa.n)

    Find[lga.Repr, Witness.`'n`.T]
    Find[lga.Repr, Witness.`'n`.T].apply(lga.to(aa)) should be(aa.n)
    Find[A, Witness.`'n`.T].apply(aa) should be(aa.n)

    Find[C, Witness.`'a`.T].apply(c) should be(aa)

    Find[C, Witness.`'n`.T].apply(c) should be(aa.n)
    Find[C, Witness.`'s`.T].apply(c) should be(b.s)
  }
}

object FindTest {
  case class A(n: Int)
  case class B(s: String)
  case class C(a: A, b: B)
  case class E(b: Boolean)
  case class F(c: C, e: E)
}


object console {
  case class A(n: Int)
  case class B(s: String)
  case class C(a: A, b: B)

  import shapeless._

  val lga = LabelledGeneric[A]
  val lgb = LabelledGeneric[B]
  val lgc = LabelledGeneric[C]

}
