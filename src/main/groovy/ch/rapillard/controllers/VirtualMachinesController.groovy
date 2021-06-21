package ch.rapillard.controllers

import ch.rapillard.domain.VirtualMachine
import ch.rapillard.services.VirshService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse

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
    @Operation(summary = "Get a list of all the Virtual Machines")
    @ApiResponse(responseCode = "200", description = "The list")
    @Get(uri="/", produces="application/json")
    List<VirtualMachine> index() {
        vmService.getAll();
    }

    @Operation(summary = "Start a Virtual Machines")
    @ApiResponse(responseCode = "200", description = "True if everything is ok")
    @Post(uri="/start", produces="application/json")
    boolean startVm(VirtualMachine virtualMachine) {
        vmService.start(virtualMachine)
    }

    @Operation(summary = "Stop a Virtual Machines")
    @ApiResponse(responseCode = "200", description = "True if everything is ok")
    @Post(uri="/stop", produces="application/json")
    boolean stopVm(StopVirtualMachineCommand cmd) {
        vmService.stop(cmd.virtualMachine, cmd.destroy)
    }
}

class StopVirtualMachineCommand {
    VirtualMachine virtualMachine
    boolean destroy
}