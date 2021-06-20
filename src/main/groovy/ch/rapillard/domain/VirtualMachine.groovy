package ch.rapillard.domain

import groovy.transform.Canonical

@Canonical
class VirtualMachine {
    public int Id
    public String Name
    public VirtualMachineState State
}
