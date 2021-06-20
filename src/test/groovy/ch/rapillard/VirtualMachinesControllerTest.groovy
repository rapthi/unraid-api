package ch.rapillard

import ch.rapillard.domain.VirtualMachine
import ch.rapillard.services.VirshService
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import io.micronaut.http.client.annotation.*
import javax.inject.Inject
import io.micronaut.http.HttpRequest

@MicronautTest
public class VirtualMachinesControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client

    @Inject
    VirshService vmService

    @Test
    void testIndex() throws Exception {
        assert client.toBlocking().exchange("/virtualMachines").status() == HttpStatus.OK
    }

    @Test
    void testContentOfVms() throws Exception {
        def machines = vmService.getAll()
        HttpRequest request = HttpRequest.GET("/virtualMachines")
        List<VirtualMachine> response = client
                .toBlocking()
                .retrieve(request, Argument.of(List.class, VirtualMachine.class))

        assert machines.size() == response.size()
    }
}
