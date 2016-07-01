package cloudlab.ops;

import java.util.logging.Logger;

import cloudlab.TestOpsProto.TestGrpc;
import io.grpc.ServerBuilder;

/*TestOpsServer: The service TestOps is binded to this server's port*/

public class TestOpsServer {
    private static final Logger logger = Logger.getLogger(TestOpsServer.class.getName());

    private int port = 50053;
    private io.grpc.Server server;

    private void start() throws Exception {
        server = ServerBuilder.forPort(port).addService(TestGrpc.bindService(new TestOpsImpl()))
                .build().start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                TestOpsServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    // Await termination on the main thread since the grpc library uses daemon
    // threads.

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        final TestOpsServer server = new TestOpsServer();
        server.start();
        server.blockUntilShutdown();
    }
}
