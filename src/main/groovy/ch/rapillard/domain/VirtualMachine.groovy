package ch.rapillard.domain

import groovy.transform.Canonical

@Canonical
class VirtualMachine {
    int Id
    String Name
    VirtualMachineState State
}
