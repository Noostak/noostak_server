package org.noostak.server.group.config;

import org.noostak.server.group.application.InviteCodeGenerator;
import org.noostak.server.group.domain.DefaultInviteCodePolicy;
import org.noostak.server.group.domain.InviteCodePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class InviteCodeGeneratorConfig {

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    public InviteCodePolicy inviteCodePolicy() {
        return new DefaultInviteCodePolicy();
    }

    @Bean
    public InviteCodeGenerator inviteCodeGenerator(SecureRandom random, InviteCodePolicy policy) {
        return new InviteCodeGenerator(random, policy);
    }
}
