package io.abp.users.services

import io.abp.users.domain.{User => DUser}
import io.abp.users.effects.idGenerator.IdGenerator
import users.User.Error._
import zio._
import zio.clock.Clock
import zio.logging._
import zio.telemetry.opentracing.OpenTracing

package object users {
  type UserService[Env] = Has[User.Service[Env]]

  object User {
    trait Service[Env] extends Serializable {
      def all: ZIO[Env, AllError, List[DUser]]
      def get(id: DUser.Id): ZIO[Env, GetError, Option[DUser]]
      def getByName(name: String): ZIO[Env, GetByNameError, List[DUser]]
      def create(name: String): ZIO[Env, CreateError, DUser]
    }
    sealed trait Error extends Throwable
    object Error {
      sealed trait AllError extends Error
      object AllError {
        case class TechnicalError(cause: Throwable) extends AllError
      }
      sealed trait GetError extends Error
      object GetError {
        case class TechnicalError(cause: Throwable) extends GetError
      }
      sealed trait GetByNameError extends Error
      object GetByNameError {
        case class TechnicalError(cause: Throwable) extends GetByNameError
      }
      sealed trait CreateError extends Error
      object CreateError {
        case class TechnicalError(cause: Throwable) extends CreateError
      }
    }

    import interpreters._

    def inMemory(users: Ref[Map[DUser.Id, DUser]]): Service[IdGenerator with Clock] =
      InMemory.interpreter(users)
    def logging[Env](underlying: User.Service[Env]): Service[Env with Logging] =
      Logging.interpreter[Env](underlying)
    def tracing[Env](underlying: User.Service[Env]): Service[Env with OpenTracing] =
      Tracing.interpreter[Env](underlying)
  }

  import User.Error._

  def getUser[Env: Tagged](id: DUser.Id): ZIO[Env with UserService[Env], GetError, Option[DUser]] =
    ZIO.accessM(_.get.get(id))

  def getUsersByName[Env: Tagged](name: String): ZIO[Env with UserService[Env], GetByNameError, List[DUser]] =
    ZIO.accessM(_.get.getByName(name))

  def createUser[Env: Tagged](name: String): ZIO[Env with UserService[Env], CreateError, DUser] =
    ZIO.accessM(_.get.create(name))

  def allUsers[Env: Tagged](): ZIO[Env with UserService[Env], AllError, List[DUser]] =
    ZIO.accessM(_.get.all)
}
