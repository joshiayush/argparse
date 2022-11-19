package Argparse;

import java.io.File;

class ActionsContainer {
    ActionsContainer(String description, String prefixChars) {
        /* All the actions need to be registered here. */
    }
}

class ArgumentParser extends ActionsContainer {
    private String progName;
    private String progUsage;
    private boolean addHelp;

    public ArgumentParser(String[] sysArgs, String progName, String progUsage, String description,
            String prefixChars, boolean addHelp) {
        super(description, prefixChars);
        if (progName == null || progName.isEmpty()) {
            this.progName = getBaseName(sysArgs[0]);
        }
        if (progUsage == null) {
            this.progUsage = "";
        }
        this.addHelp = addHelp;

        char defaultPrefix = prefixChars.charAt(0);
        if (this.addHelp) {
            // Add help option here.
        }
    }

    private String getBaseName(String fileName) {
        return new File(fileName).getName();
    }
}
