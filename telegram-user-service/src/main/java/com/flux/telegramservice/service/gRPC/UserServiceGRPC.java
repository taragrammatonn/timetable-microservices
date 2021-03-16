package com.flux.telegramservice.service.gRPC;

import com.flux.telegramservice.UserServiceGrpc;
import com.flux.telegramservice.UserServiceOuterClass;
import com.flux.telegramservice.entity.UserVO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceGRPC {

    public UserVO getUserVO(Long chatId) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8888)
                .usePlaintext()
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);
        UserServiceOuterClass.getUserByChatId request = UserServiceOuterClass.getUserByChatId.newBuilder()
                .setUserChatId(chatId)
                .build();

        UserServiceOuterClass.getUserByChatIdResponse response = stub.getUserVO(request);

        channel.shutdown();

        return new UserVO(
                response.getFName(),
                response.getUserLanguage(),
                response.getIsDefined()
        );
    }
}
