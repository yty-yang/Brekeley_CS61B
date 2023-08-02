package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 * Young
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        //what if args is empty?
        if (args == null) {
            throw new GitletException("Please enter a command.");
        }

        String firstArg = args[0];
        String message;

        switch (firstArg) {
            case "init":
                //handle the `init` command
                oprand_check(args, 1);
                Repository.init();
                break;
            case "add":
                //handle the `add [filename]` command
                oprand_check(args, 2);

                message = args[1];
                Repository.IintCheck();
                Repository.ADD(message);
                break;
            //FILL THE REST IN
            case "commit":
                oprand_check(args, 2);

                message = args[1];
                Repository.IintCheck();
                Repository.COMMIT(message);
                break;
            case "rm":
                oprand_check(args, 2);

                message = args[1];
                Repository.IintCheck();
                Repository.RM(message);
                break;
            case "log":
                oprand_check(args, 1);

                Repository.IintCheck();
                Repository.LOG();
                break;
            case "Global-log":
                oprand_check(args, 1);

                Repository.IintCheck();
                Repository.GlobalLOG();
                break;
            case "find":
                oprand_check(args, 2);

                message = args[1];
                Repository.IintCheck();
                Repository.FIND(message);
                break;
            case "status":
                oprand_check(args, 1);

                Repository.IintCheck();
                Repository.STATUS();
                break;
            case "checkout":
                Repository.IintCheck();
                break;
            case "branch":
                Repository.IintCheck();
                break;
            case "rm-branch":
                Repository.IintCheck();
                break;
            case "reset":
                Repository.IintCheck();
                break;
            case "merge":
                Repository.IintCheck();
                break;
            default:
                throw new GitletException("No command with that name exists.");
        }
    }

    private static void oprand_check(String[] args, int n) {
        if (args.length != n) {
            throw new GitletException("Incorrect operands.");
        }
    }
}
