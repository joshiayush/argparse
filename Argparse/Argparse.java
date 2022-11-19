package Argparse;

import java.io.File;
import java.util.ArrayList;

enum ArgparseOptionType {
    ARGPARSE_OPT_END,
    ARGPARSE_OPT_GROUP,
    ARGPARSE_OPT_BOOLEAN,
    ARGPARSE_OPT_BIT,
    ARGPARSE_OPT_INTEGER,
    ARGPARSE_OPT_FLOAT,
    ARGPARSE_OPT_STRING,
}

class ArgparseOption {
    public String shortName;
    public String longName;
    public String value;
    public String help;
    public ArgparseOptionType optionType;

    public ArgparseOption(String shortName, String longName, String value, ArgparseOptionType optionType, String help) {
        this.shortName = shortName;
        this.longName = longName;
        this.value = value;
        this.optionType = optionType;
        this.help = help;
    }
}

class Argparse {
    private String progname;
    private String usage;

    /** A description of the command line software at the end of the usage. */
    private String description;

    /** A description of the command line software at the end of the description. */
    private String epilog;

    private ArrayList<ArgparseOption> options;

    Argparse(String[] sysargs, String progname, String usage, String description, String epilog) {
        if (progname == null || progname.isEmpty())
            this.progname = new File(sysargs[0]).getName();
        this.usage = usage;
        this.description = description;
        this.epilog = epilog;
    }

    public void addArgument(final ArgparseOption argparseOption) {
        options.add(argparseOption);
    }

    private String formatUsage() {
        String formattedOptionsString = "";
        for (final ArgparseOption option : this.options) {
            if ((option.shortName == null || option.shortName.isEmpty())
                    && (option.longName == null || option.longName.isEmpty()))
                continue;
            formattedOptionsString += "[";
            if (option.shortName != null && !option.shortName.isEmpty())
                formattedOptionsString += option.shortName + "|";
            if (option.longName != null && !option.longName.isEmpty())
                formattedOptionsString += option.longName;
            formattedOptionsString += "]";
        }
        String formattedUsageString = "";
        formattedUsageString += "Usage: " + this.progname + " " + formattedOptionsString + "\n";
        formattedUsageString += "\n";
        int indentWidth = 4;
        for (final ArgparseOption option : options) {
            if ((option.shortName == null || option.shortName.isEmpty())
                    && (option.longName == null || option.longName.isEmpty()))
                continue;
            for (int i = 0; i < indentWidth; ++i)
                formattedUsageString += " ";
            if (option.shortName != null && !option.shortName.isEmpty())
                formattedUsageString += option.shortName;
            if (option.longName != null && !option.longName.isEmpty())
                formattedUsageString += ", " + option.longName;
            if (option.help != null && !option.help.isEmpty()) {
                for (int i = 0; i < indentWidth; ++i)
                    formattedUsageString += " ";
                formattedUsageString += option.help;
            }
        }
        return formattedUsageString;
    }

    public void printUsage() {
        if (this.usage != null)
            System.err.println(this.usage);
        else
            System.err.println(this.formatUsage());
        System.err.println();
        if (!this.description.isEmpty())
            System.err.println(this.description);
        if (!this.epilog.isEmpty())
            System.err.println(this.epilog);
    }
}