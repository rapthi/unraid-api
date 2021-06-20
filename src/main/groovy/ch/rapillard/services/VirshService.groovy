package ch.rapillard.services

import ch.rapillard.SshConfig
import ch.rapillard.domain.VirtualMachine
import ch.rapillard.domain.VirtualMachineState
import groovy.util.logging.Slf4j

import javax.inject.Inject

import static com.aestasit.infrastructure.ssh.DefaultSsh.*
import javax.inject.Singleton


@Singleton
@Slf4j
class VirshService {

    @Inject
    SshConfig sshConfig

    /**
     * Get all the available virtual machines
     * @return A List of VirtualMachine
     */
    List<VirtualMachine>  getAll() {
        String consoleOutput = ''
        options.trustUnknownHosts = true
        remoteSession {
            host = sshConfig.host
            user = sshConfig.user
            password = sshConfig.password

            consoleOutput = exec('virsh list --all').output
        }
        extractVirtualMachinesFromConsoleOutput(consoleOutput)
    }

    /**
     * Clean the console output header
     * @param lines An array of console output lines
     * @return An array that contains everything except the header :)
     */
    private static String[] dropHeader(String[] lines) {
        // the 2 first lines are unneeded header
        lines.drop(2)
    }

    /**
     * Extract the console output into a collection of VirtualMachine object
     * @param consoleOutput A String that was output by the virsh command
     * @return A List of VirtualMachine
     */
    private static List<VirtualMachine> extractVirtualMachinesFromConsoleOutput(String consoleOutput) {
        List<VirtualMachine> virtualMachines = []
        def linesOfText = dropHeader(consoleOutput.split("\\n"))

        linesOfText.each {
            def words = it.trim().split('\\W') as List

            if (words.size() <= 1) {
                return
            }

            VirtualMachine vm = new VirtualMachine()
            vm.Id = extractIdentifier(words[0])
            // Remove the first line of the array, because the id was found
            words.pop()
            vm.State = extractStatus(words)
            // Clean the status words
            while (words.last() != '') {
                words.removeLast()
            }

            vm.Name = extractName(words)

            virtualMachines << vm
        }
        virtualMachines
    }

    /**
     * Extract the name from the console output
     * @param words A list of words that has been detected
     * @return The extracted name as String
     */
    private static String extractName(List<String> words) {
        String name = ''
        words.each {
            if (it != null && it.trim() != '') {
                name += ' ' + it
            }
        }

        name.trim()
    }

    /**
     * Extract the status of the virtual machine
     * @param words
     * @return The status as VirtualMachineState
     */
    private static VirtualMachineState extractStatus(List<String> words) {
        // Find the state of the machine
        // It could be 'running' or 'shut off'
        String state = words.last()?.toUpperCase()
        // if we are in the case of 'shut off', the last
        String penultimateWord = words[words.size() - 2]?.trim()
        if (penultimateWord != '') {
            state = penultimateWord.toUpperCase() + '_' + state
        }
        state as VirtualMachineState
    }

    /**
     * Extract the ID of the machine. If the machine is not running the ID is always 0
     * @param word
     * @return The id of the machine or 0 if it's not running
     */
    private static int extractIdentifier(String word) {
        // As we trimmed the line, the first word is the Id. It could be an int or a '-' sign. In that case,
        // we use a 0
        word.isInteger() ? word.toInteger() : 0
    }
}