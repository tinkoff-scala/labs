package labs.lab10_monads.client

import cats.effect.IO

// What problems does the Test have? How we can solve them? Is it reasonable to use asynchronous tests here

trait UptimeClient {
  def getUptime(hostname: String): IO[Int]
}

class UptimeService(client: UptimeClient) {
  def getTotalUptime(hosts: List[String]): IO[Int] = ???
}

class TestUptimeClient(hostsUptime: Map[String, Int]) extends UptimeClient {
  override def getUptime(hostname: String): IO[Int] = ???
}

object Test extends App {
  val uptimes = Map("host1" -> 10, "host2" -> 5)
  val client = new TestUptimeClient(uptimes)
  val service = new UptimeService(client)
  assert(service.getTotalUptime(List("host1", "host2")) == 15)
}

// Rework