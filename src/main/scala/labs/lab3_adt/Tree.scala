package labs.lab3_adt

import labs.lab3_adt.Tree.{Branch, Leaf, depth, maximum, size}

import scala.annotation.tailrec

sealed trait Tree[A]
object Tree {
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  //1. returns number of leaf nodes in a tree
  def size[A](tree: Tree[A]): Int = tree match {
    case Leaf(_)             => 1
    case Branch(left, right) => size(left) + size(right)
  }

  //2. returns the maximum element in a Tree[Int]
  def maximum(tree: Tree[Int]): Int = tree match {
    case Leaf(value) => value
    case Branch(left, right) =>
      val maxLeft = maximum(left)
      val maxRight = maximum(right)
      maxLeft max maxRight
  }

  //3. returns maximum path from root to leaf node
  def depth[A](tree: Tree[A]): Int = tree match {
    case Leaf(_) => 1
    case Branch(left, right) =>
      (depth(left) max depth(right)) + 1
  }
}

object T extends App {
  val tree = Branch(
    Branch(
      Branch(
        Branch(
          Leaf(1),
          Leaf(2)
        ),
        Branch(
          Branch(
            Leaf(10),
            Leaf(7)
          ),
          Leaf(9)
        )
      ),
      Leaf(100)
    ),
    Leaf(0)
  )

  println(size(tree))
}
