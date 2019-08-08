package org.eddard.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.eddard.grpc.Beat;
import org.eddard.grpc.Heart;
import org.eddard.grpc.HeartBeatMessageGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GRPCServer implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(GRPCServer.class);

    private Server server;

    @Value("#{grpc.server.port}")
    private Integer GRPC_SERVER_PORT;

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
        logger.info("Server has been started.");
    }

    @Override
    public void destroy() throws Exception {
        server.shutdown();

        logger.info("Server has been shutdown.");
    }

    private void start() throws IOException {

        server = ServerBuilder.forPort(GRPC_SERVER_PORT)
                .addService(new HeartBeatImpl())
                .build()
                .start();
    }

    private static class HeartBeatImpl extends HeartBeatMessageGrpc.HeartBeatMessageImplBase {

        private final Logger logger = LoggerFactory.getLogger(HeartBeatImpl.class);

        private Integer id = 0;

        @Override
        public void getHeartBeat(Heart request, StreamObserver<Beat> responseObserver) {

            logger.info(request.getId() + ": " + request.getMessage());

            //클라이언트에서 메시지를 받으면 응답 메시지를 생성한다.
            Beat beat = Beat.newBuilder()
                    .setId(++id)
                    .setMessage("Response")
                    .build();

            responseObserver.onNext(beat);
            responseObserver.onCompleted();
        }
    }
}
