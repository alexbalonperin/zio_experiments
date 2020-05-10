package io.abp.users.programs

import java.io.IOException
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneOffset

import io.abp.users.domain.User
import io.abp.users.services.users
import io.abp.users.services.users._
import zio._

object UserProgram {

  //The existence check wouldn't work in a concurrent system. We need semantic locking.
  //TODO: explore ZIO.STM and ZIO.Ref
  def createUser[Env: Tagged](name: String): ZIO[Env with UserService[Env], ProgramError, User.Id] =
    for {
      //result <- getUsersByName(name).mapError(ProgramError.UserError)
      user <- users.createUser(name).mapError(ProgramError.UserError)
      //user <-
      //  if (result.isEmpty) users.createUser(name).mapError(ProgramError.UserError)
      //  else ZIO.fail(ProgramError.UserAlreadyExists)
    } yield user.id

  def getUser[Env: Tagged](id: User.Id): ZIO[Env with UserService[Env], ProgramError, Option[User]] =
    users.getUser(id).mapError(ProgramError.UserError)

  def getAllUsers[Env: Tagged](): ZIO[Env with UserService[Env], ProgramError, List[User]] =
    allUsers.mapError(ProgramError.UserError)

  def getUsersCreatedBefore[Env: Tagged](
      instant: Instant
  ): ZIO[Env with UserService[Env], ProgramError, List[User]] =
    allUsers
      .mapError(ProgramError.UserError)
      .map(_.filter(_.createdAt.atZoneSameInstant(ZoneOffset.UTC).toInstant.isBefore(instant)))

  trait ProgramError extends Throwable
  object ProgramError {
    case class ConsoleError(underlying: IOException) extends ProgramError
    case class ClockError(underlying: DateTimeException) extends ProgramError
    case class UserError(underlying: users.User.Error) extends ProgramError
    case object UserAlreadyExists extends ProgramError
  }

}
