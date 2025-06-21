package com.littlebank.finance.global.business;

import com.littlebank.finance.domain.chat.dto.UserFriendInfoDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatPolicy {

    public static String getInvitationMessage(UserFriendInfoDto agent, List<UserFriendInfoDto> targets) {
        StringBuilder message = new StringBuilder();
        message.append(agent.getIsFriend() ? agent.getCustomName() : agent.getName() + "님이 ")
                .append(targets.stream()
                        .map(t -> t.getIsFriend() ? t.getCustomName() : t.getName())
                        .collect(Collectors.joining(", ")) + "님을 ")
                .append("초대하였습니다");
        return message.toString();
    }

    public static String getLeaveMessage(UserFriendInfoDto agent) {
        StringBuilder message = new StringBuilder();
        message.append(agent.getIsFriend() ? agent.getCustomName() : agent.getName() + "님이 ")
                .append("방을 나가셨습니다");
        return message.toString();
    }

}
