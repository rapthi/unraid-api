package ch.rapillard

import groovy.transform.Canonical
import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.ConfigurationProperties

import javax.validation.constraints.NotBlank

@ConfigurationProperties("ssh")
@Canonical
class SshConfig {
    final String host
    final String user
    final String password

    @ConfigurationInject
    SshConfig(final String host, final String user, final String password) {
        this.host = host
        this.user = user
        this.password = password
    }
}
