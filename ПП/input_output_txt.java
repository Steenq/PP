//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.*;

public class Main {
    public static void main(String[] args) {
        StringBuilder ss = new StringBuilder();
        try(FileReader r = new FileReader("input.txt")) {
            int c;
            while((c = r.read()) != -1) {
                ss.append((char)c);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        String s = ss.toString().toUpperCase();
        System.out.println(s);
        try(FileWriter w = new FileWriter("output.txt", false)) {
            w.write(s);
            w.append('\n');
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
