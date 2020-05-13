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
  def createUser[Env: Tagged](name: String) = ZIO.accessM[Env with UserService[Env]]( svc =>
    for {
      result <- svc.get.getByName(name).mapError(ProgramError.UserError)
      user <-
        if (result.isEmpty) svc.get.create(name).mapError(ProgramError.UserError)
        else ZIO.fail(ProgramError.UserAlreadyExists)
    } yield user.id
  )

  def getUser[Env: Tagged](id: User.Id) = ZIO.accessM[Env with UserService[Env]](
    _.get.get(id).mapError(ProgramError.UserError)
  )

  def getAllUsers[Env: Tagged]() = ZIO.accessM[Env with UserService[Env]](
    _.get.all.mapError(ProgramError.UserError)
  )

  def getUsersCreatedBefore[Env: Tagged](instant: Instant) = ZIO.accessM[Env with UserService[Env]](
    _.get.all
      .mapError(ProgramError.UserError)
      .map(_.filter(_.createdAt.atZoneSameInstant(ZoneOffset.UTC).toInstant.isBefore(instant)))
  )

  trait ProgramError extends Throwable
  object ProgramError {
    case class ConsoleError(underlying: IOException) extends ProgramError
    case class ClockError(underlying: DateTimeException) extends ProgramError
    case class UserError(underlying: users.User.Error) extends ProgramError
    case object UserAlreadyExists extends ProgramError
  }

}
