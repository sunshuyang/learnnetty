package com.netty.learnnetty15.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloWorldClient {

    public static final String HOST = System.getProperty("host","127.0.0.1");
    public static final int PORT = Integer.parseInt(System.getProperty("port","8080"));
    public static final int SIZE = Integer.parseInt(System.getProperty("size","256"));

    public static void main(String[] args){
        initChannel();
    }

    public static void initChannel(){
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder",new StringDecoder());
                            p.addLast("encoder",new StringEncoder());
                            p.addLast(new HelloWorldClientHandler());
                            p.addLast(new HelloWorld2ClientHandler());
                        }
                    });
            ChannelFuture future = b.connect(HOST,PORT).sync();
            future.channel().writeAndFlush("hello Netty,Test attributeMap");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
        }
    }
}