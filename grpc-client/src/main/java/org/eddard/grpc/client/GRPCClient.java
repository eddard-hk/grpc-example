package org.eddard.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.eddard.grpc.Beat;
import org.eddard.grpc.Heart;
import org.eddard.grpc.HeartBeatMessageGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GRPCClient implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(GRPCClient.class);

    private ManagedChannel channel;
    private HeartBeatMessageGrpc.HeartBeatMessageBlockingStub blockingStub;

    private Integer id;

    @Value("${grpc.host}")
    private String GRPC_HOST;

    @Value("${grpc.port}")
    private Integer GRPC_PORT;

    @Override
    public void afterPropertiesSet() throws Exception {

        channel = ManagedChannelBuilder.forAddress(GRPC_HOST, GRPC_PORT)
                .usePlaintext()
                .build();

        blockingStub = HeartBeatMessageGrpc.newBlockingStub(channel);

        id = 0;
    }

    @Override
    public void destroy() throws Exception {
        channel.shutdown();
    }

    public void sendHeartBeat(String message) {
        Heart heart = Heart.newBuilder()
                .setId(++id)
                .setMessage(message)
                .build();

        Beat beat;

        try {
            beat = blockingStub.getHeartBeat(heart);

            logger.info("" + beat.getId());
            logger.info(beat.getMessage());

        } catch (StatusRuntimeException ex) {
            logger.warn("RPC failed: {0}", ex.getStatus());
            return;
        }

        logger.info("Response: " + beat.getMessage());
    }
}
