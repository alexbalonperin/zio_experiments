//package io.abp.users.services.users
//
//import io.abp.users.domain.User
//import io.abp.users.effects.idGenerator._
//import io.abp.users.fixtures._
//import io.abp.users.generators._
//import io.abp.users.mocks._
//import io.abp.users.services.users.{User => UserService}
//import io.abp.users.TestEnvironments
//import zio._
//import zio.test._
//import zio.test.Assertion._
//import zio.test.environment._
//import zio.test.Gen._
//
//object UserServiceSpec extends DefaultRunnableSpec {
//  def makeUserService(input: Map[User.Id, User] = Map()) = UserService.inMemory(input)
//
//  override def spec =
//    suite("UserService")(
//      suite("create")(
//        testM("should create a new user and return it") {
//          val userService = makeUserService()
//          checkM(anyString) { name =>
//            val expected = User(fixedUserId, name, fixedDateTime)
//            userService
//              .create(name)
//              .map { result => assert(result)(equalTo(expected)) }
//              .provideLayer(TestEnvironments().env)
//          }
//        }
//      ),
//      suite("get")(
//        testM("should return the user with the corresponding id") {
//          checkM(Gen.listOfN(10)(userGen)) { users =>
//            val userService = makeUserService(users.toM)
//            val name = "Alex"
//            val expected = Some(User(fixedUserId, name, fixedDateTime))
//            (for {
//              _ <- userService.create(name)
//              result <- userService.get(fixedUserId)
//            } yield assert(result)(equalTo(expected)))
//              .provideLayer(TestEnvironments().env)
//          }.provideLayer(testEnvironment ++ testIdGeneratorMock(fixedUserId))
//        }
//      ),
//      suite("getByName")(
//        testM("should return the list of users with the corresponding name") {
//          checkM(Gen.listOfN(10)(userGen)) { users =>
//            val userService = makeUserService(users.toM)
//            val name = "Alex"
//            val expected = List(User(fixedUserId, name, fixedDateTime))
//            (for {
//              _ <- userService.create(name)
//              result <- userService.getByName(name)
//            } yield assert(result)(equalTo(expected)))
//              .provideLayer(TestEnvironments().env)
//          }.provideLayer(testEnvironment ++ testIdGeneratorMock(fixedUserId))
//        }
//      ),
//      suite("all")(
//        testM("should return the list of all users") {
//          checkM(Gen.listOfN(10)(userGen)) { users =>
//            val userService = makeUserService(users.toM)
//            val name = "Alex"
//            (for {
//              user <- userService.create(name)
//              expected <- ZIO.succeed(user +: users)
//              result <- userService.all
//            } yield assert(result)(hasSameElements(expected)))
//              .provideLayer(
//                TestEnvironments(
//                  testIdGenerator = IdGenerator.live
//                ).env
//              )
//          }.provideLayer(testEnvironment ++ IdGenerator.live)
//        }
//      )
//    )
//
//  implicit class UserOps(val users: List[User]) extends AnyVal {
//    def toM = users.map(u => (u.id, u)).toMap
//  }
//}
