package me.dkz.plugin.artemis.artemisnexus.clan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Invite {

    public final UUID clan;
    public Date date = new Date();
    public final UUID sender;
    public final UUID target;

}
