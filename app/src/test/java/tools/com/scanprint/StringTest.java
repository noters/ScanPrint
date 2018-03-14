package tools.com.scanprint;

import org.junit.Test;

public class StringTest {

    @Test
    public void str() {
        String in = "abc\n";
        System.out.println("------");
        System.out.println(in);
        System.out.println("------");

        System.out.println(in.endsWith("\n"));
        String out = in.replace("\n", "");
        System.out.println("------");
        System.out.println(out);
        System.out.println("------");

        String[] ab = "a@b@c@".split("@");
        System.out.println(ab.length);
    }
}
