package client;

import com.google.protobuf.util.Timestamps;
import com.learn.stubs.message.MessageRequest;
import com.learn.stubs.message.MessageServiceGrpc;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class MessageClient {

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forTarget("localhost:50050").usePlaintext().build();
        var messageServiceBlockingStub = MessageServiceGrpc.newBlockingStub(channel);
        var request = MessageRequest.newBuilder()
                .setValue("Message from client")
                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                .build();
        var response = messageServiceBlockingStub.getMessage(request);
        System.out.println("Response value: " + response.getValue() + " timestamp: " + response.getTimestamp());
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
