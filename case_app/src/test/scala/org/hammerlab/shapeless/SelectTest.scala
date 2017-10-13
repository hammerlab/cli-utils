package org.hammerlab.shapeless

import org.hammerlab.test.Suite

object SelectTest {

  case class A(n: Int)

  class A2(m: Int) extends A(m)

  case class B(s: String)

  class B2(t: String) extends B(t)

  case class Foo(a: A2, b: B2)

}

class SelectTest
  extends Suite {

  import SelectTest._

  test("direct access") {
    implicitly[Select[Foo, A2]].apply(Foo(new A2(123), new B2("abc"))) should be(new A2(123))
  }

  test("covariant access") {
    implicitly[Select[Foo, A]].apply(Foo(new A2(123), new B2("abc"))) should be(A(123))
  }
}
