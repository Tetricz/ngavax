package ngavax.app;


class LOG {
    private static LOG l = null;
    private static boolean debug = false;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public static LOG getInstance(){
        if(l == null){
            l = new LOG();
        }
        return l;
    }

    static public void toggleDebug(){
        debug = !debug;
    }

    static public void info(Object o){
        String msg = o.toString();
        System.out.println( ANSI_YELLOW + "Info:   " + ANSI_GREEN + msg);
    }

    static public void error(Object o){
        String msg = o.toString();
        System.out.println( ANSI_RED + "Error:  " + ANSI_GREEN + msg);
    }

    static public void debug(Object o){
        if(debug){
            String msg = o.toString();
            System.out.println( ANSI_BLUE + "Debug:  " + ANSI_GREEN + msg);
        }
    }
}
