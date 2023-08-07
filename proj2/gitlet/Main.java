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
                Repository.INIT();
                break;
            case "add":
                //handle the `add [filename]` command
                oprand_check(args, 2);

                message = args[1];
                Repository.InitCheck();
                Repository.ADD(message);
                break;
            //FILL THE REST IN
            case "commit":
                oprand_check(args, 2);

                message = args[1];
                Repository.InitCheck();
                Repository.COMMIT(message);
                break;
            case "rm":
                oprand_check(args, 2);

                message = args[1];
                Repository.InitCheck();
                Repository.RM(message);
                break;
            case "log":
                oprand_check(args, 1);

                Repository.InitCheck();
                Repository.LOG();
                break;
            case "Global-log":
                oprand_check(args, 1);

                Repository.InitCheck();
                Repository.GlobalLOG();
                break;
            case "find":
                oprand_check(args, 2);

                message = args[1];
                Repository.InitCheck();
                Repository.FIND(message);
                break;
            case "status":
                oprand_check(args, 1);

                Repository.InitCheck();
                Repository.STATUS();
                break;
            case "checkout":
                if (args.length == 2) {
                    Repository.InitCheck();
                    message = args[1];
                    Repository.CHECKOUT_branch(message);
                    break;
                }
                if (args.length == 3) {
                    Repository.InitCheck();
                    message = args[2];
                    Repository.CHECKOUT_file(message);
                    break;
                }
                if (args.length == 4) {
                    Repository.InitCheck();
                    Repository.CHECKOUT_file(args[1], args[3]);
                    break;
                }
                throw new GitletException("No command with that name exists.");
            case "branch":
                Repository.InitCheck();
                oprand_check(args, 2);

                message = args[1];
                Repository.InitCheck();
                Repository.BRANCH(message);
                break;
            case "rm-branch":
                Repository.InitCheck();
                oprand_check(args, 2);

                message = args[1];
                Repository.InitCheck();
                Repository.RMBRANCH(message);
                break;
            case "reset":
                Repository.InitCheck();
                oprand_check(args, 2);

                message = args[1];
                Repository.InitCheck();
                Repository.RESET(message);
                break;
            case "merge":
                Repository.InitCheck();
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
