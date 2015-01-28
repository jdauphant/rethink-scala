package com.rethinkscala.net

import com.typesafe.scalalogging.LazyLogging
import org.jboss.netty.channel.{ChannelHandlerContext, ExceptionEvent, MessageEvent, SimpleChannelUpstreamHandler}
import org.jboss.netty.handler.queue.BufferedWriteHandler
import ql2.Ql2.Response

/**
 * Created with IntelliJ IDEA.
 * User: keyston
 * Date: 8/16/14
 * Time: 11:45 AM
 *
 */


class RethinkChannelHandler[T] extends SimpleChannelUpstreamHandler {
  type Handler = ConnectionAttachment[T]

  implicit def channelHandlerContext2Promise(ctx: ChannelHandlerContext) = Some(ctx.getChannel.getAttachment.asInstanceOf[Handler])

  override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent) {
    ctx.map(_.failure(e.getCause))
  }
}

class ProtoChannelHandler extends RethinkChannelHandler[Response]
{
  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) {
    val response = e.getMessage.asInstanceOf[Response]
    ctx.map(_.handle(response.getToken, response))
  }
}

class JsonChannelHandler extends RethinkChannelHandler[String]
{
  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) {
    val (tokenId, response) = e.getMessage.asInstanceOf[(Long, String)]
    ctx.map(_.handle(tokenId, response))
  }
}


