package service;

import com.google.protobuf.util.Timestamps;
import com.learn.stubs.message.MessageRequest;
import com.learn.stubs.message.MessageResponse;
import com.learn.stubs.message.MessageServiceGrpc;
import io.grpc.stub.StreamObserver;

public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
    @Override
    public void getMessage(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
        var response = MessageResponse.newBuilder()
                .setValue("Message from server. Answer for %s".formatted(request.getValue()))
                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
