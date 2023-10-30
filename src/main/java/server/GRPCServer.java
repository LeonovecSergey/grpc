package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.MessageServiceImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GRPCServer {

    private static final Logger log = Logger.getLogger(GRPCServer.class.getName());

    private Server server;

    public void startServer() {
        int port = 50050;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new MessageServiceImpl())
                    .build()
                    .start();

            log.info("Server started on port %s".formatted(port));

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    log.info("Clean server shutdown in case JVM was shutdown");
                    try {
                        GRPCServer.this.stopServer();
                    } catch (InterruptedException e) {
                        log.log(Level.SEVERE, "Server shutdown interrupted", e);
                    }
                }
            });
        } catch (IOException e) {
            log.log(Level.SEVERE, "Server didn't start", e);
        }
    }

    public void stopServer() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var grpcServer = new GRPCServer();
        grpcServer.startServer();
        grpcServer.blockUntilShutdown();
    }
}
