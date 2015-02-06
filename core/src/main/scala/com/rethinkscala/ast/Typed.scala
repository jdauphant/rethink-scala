package com.rethinkscala.ast

import com.rethinkscala._


sealed trait DataType {

  def name: String
}


trait CanManipulate[P<:Pluck,M<:Merge,W<:Without] extends Typed{


  type CM = CanManipulate[P,M,W]

  //def pluck(attrs: String*):P

  def pluck(m: Map[String, Any]):P

  def without(attrs: String*):W


  def merge(other: CM):M

  def merge(other: Map[String, Any]):M

}
case object ObjectData extends DataType {
  def name = "object"
}

case object StringData extends DataType {
  def name = "string"
}

case object ArrayData extends DataType {
  def name = "array"
}


private[rethinkscala] trait Typed extends ImplicitConversions{

  // TODO : Fix me
  def term: Term = this.asInstanceOf[Term]

  val underlying = this

  def info = Info(underlying)

  def typeOf = TypeOf(underlying)

  def coerceTo(dataType: DataType) = CoerceTo(underlying, dataType)

  def unary_~ = not
  def ===(other: Typed) = eq(other)
  def !=(other: Typed) = ne(other)

  def =!=(other: Typed) = ne(other)

  def <(other: Typed) = lt(other)

  def <=(other: Typed) = lte(other)
  def >=(other: Typed) = gte(other)
  def >(other: Typed) = gt(other)


  def not = Not(underlying)

  def eq(other: Typed) = Eq(underlying, other)
  def ne(other: Typed) = Ne(underlying, other)
  def lt(other: Typed) = Lt(underlying, other)
  def lte(other: Typed) = Le(underlying, other)
  def gt(other: Typed) = Gt(underlying, other)
  def gte(other: Typed) = Ge(underlying, other)
}


object Ref{
  implicit class ScalaRef(underlying:Ref){
     def +(other: Numeric) = underlying.add(other)

     def +=(other: Numeric) = underlying.add(other)
  }
}
trait Ref extends Sequence[Any] with Numeric with Binary with Record with Literal with Strings with Geometry[UnknownGeometry]

with CanManipulate[Pluck,Merge,Without] {
  override val underlying = this

  //override def add(other: Addition): Add = AnyAdd(underlying, other)

  //def add(other: Ref): Add = Add(underlying, other)







  def merge(other: Map[String, Any]) = Merge(underlying,other)


 def merge(other: CM) = Merge(underlying,other)

  def pluck(attrs: String*) = Pluck(underlying, attrs)

  def without(attrs: String*) = Without(underlying, attrs)

  def pluck(m: Map[String, Any]) = Pluck(underlying, m)
}

trait JoinTyped[L, R] extends Typed {
  override val underlying = this

  def zip = Zip(underlying)
}


trait Multiply extends Typed {

  override val underlying = this

  def *(other: Numeric): Mul = mul(other)

  def *(other: Double): Mul = mul(other)

  def mul(other: Numeric): Mul = Mul(underlying, other)

  def mul(other: Double): Mul = Mul(underlying, other)
}

object Binary{

}


/*
trait BinaryOps{
  self:Typed=>

  def &&(other: Binary) = and(other)
  def and(other: Binary) = All(underlying, other)

  def rand(other: Binary) = All(other, underlying)

  def &>(other: Binary) = rand(other)

  // or
  def ||(other: Binary) = or(other)

  def or(other: Binary) = Or(underlying, other)

  // right or
  def >|(other: Binary) = ror(other)

  def ror(other: Binary) = Or(other, underlying)
}*/
trait Binary extends Typed {

  override  val underlying = this


  def &&(other: Binary) = and(other)
  def and(other: Binary) = All(underlying, other)

  def rand(other: Binary) = All(other, underlying)

  def &>(other: Binary) = rand(other)

  // or
  def ||(other: Binary) = or(other)

  def or(other: Binary) = Or(underlying, other)

  // right or
  def >|(other: Binary) = ror(other)

  def ror(other: Binary) = Or(other, underlying)
}


sealed trait LogicSignature
