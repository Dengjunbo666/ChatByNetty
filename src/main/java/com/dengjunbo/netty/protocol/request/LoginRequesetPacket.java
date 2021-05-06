package com.dengjunbo.netty.protocol.request;

import com.dengjunbo.netty.protocol.Packet;
import com.dengjunbo.netty.protocol.command.Command;
import lombok.Data;

@Data
public class LoginRequesetPacket extends Packet {
    private String userId;
    private String username;
    private String password;
    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
