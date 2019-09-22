import java.util.Scanner;

public class ProcessControl {

    private String preprocessString(String s)
    {
        int pos = 0;
        String str;
        while ((s.charAt(pos) == ' ') ||
                (s.charAt(pos) == '\t')) {
            if (pos == s.length() - 1) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            pos++;
        }
        if (pos >= s.length()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        if ((s.charAt(pos) != '+') &&
                (s.charAt(pos) != '-')) {
            str = '+' + s;
        }
        else {
            str = s;
        }
        return str;
    }

    public static void main(String[] args) {
        String s = "";
        String out;
        Scanner in = new Scanner(System.in);
        if (in.hasNextLine()) {
            s = in.nextLine();
        }
        else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        if (s.equals("")) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        ProcessControl pc = new ProcessControl();
        String str = pc.preprocessString(s);
        Addandminus poly = new Addandminus();
        if (poly.analyzeLexical(str,0)
                != str.length()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }

        //result
        out = poly.Differential();
        if (out.isEmpty()) {
            //System.out.print(0);
            out = out + "0";
        }
        //System.out.print("\n");
        //System.out.println(out);

        //opt 1
        Addandminus polyopt = new Addandminus();
        out = pc.preprocessString(out);
        polyopt.analyzeLexical(out,0);
        String opt = polyopt.getOpt();
        if (opt.isEmpty()) {
            opt = opt + "0";
        }
        //System.out.print("\n");
        //System.out.println(opt);

        //opt 2
        Addandminus polyopt2 = new Addandminus();
        opt = pc.preprocessString(opt);
        polyopt2.analyzeLexical(opt,0);
        String opt1 = polyopt2.getOpt();
        if (opt1.isEmpty()) {
            opt1 = opt1 + "0";
        }
        //System.out.print("\n");
        System.out.println(opt1);

    }
}
