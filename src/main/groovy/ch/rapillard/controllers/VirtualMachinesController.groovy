package ch.rapillard.controllers

import ch.rapillard.domain.VirtualMachine
import ch.rapillard.services.VirshService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

/**
 * Interact with your virtual machines
 */
@Controller("/virtualMachines")
class VirtualMachinesController {

    protected final VirshService vmService;

    VirtualMachinesController(VirshService vmService) {
        this.vmService = vmService;
    }

    /**
     * Get a list of all your VMs.
     * @return A List of VirtualMachine
     */
    @Get(uri="/", produces="application/json")
    List<VirtualMachine> index() {
        vmService.getAll();
    }
}