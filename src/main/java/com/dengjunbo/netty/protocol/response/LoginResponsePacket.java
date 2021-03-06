package com.dengjunbo.netty.protocol.response;

import com.dengjunbo.netty.protocol.Packet;
import com.dengjunbo.netty.protocol.command.Command;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {
    private boolean success;
    private String reason;
    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
