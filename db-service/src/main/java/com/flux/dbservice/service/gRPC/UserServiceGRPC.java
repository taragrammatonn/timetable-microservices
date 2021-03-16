package com.flux.dbservice.service.gRPC;

import com.flux.dbservice.entity.users.User;
import com.flux.dbservice.repository.users.UserRepository;
import com.flux.telegramservice.UserServiceGrpc;
import com.flux.telegramservice.UserServiceOuterClass;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class UserServiceGRPC extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserVO(UserServiceOuterClass.getUserByChatId request,
                          StreamObserver<UserServiceOuterClass.getUserByChatIdResponse> responseObserver) {
        User user = userRepository.findByChatId(request.getUserChatId());

        UserServiceOuterClass.getUserByChatIdResponse response = UserServiceOuterClass.getUserByChatIdResponse.newBuilder()
                .setIsDefined(user.getIsDefined())
                .setFName(user.getFName())
                .setUserLanguage(user.getUserLanguage())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
