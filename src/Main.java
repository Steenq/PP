import com.xml.SAXExample;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        SAXExample saxExample = new SAXExample();

        StringBuilder ss = new StringBuilder();

        try {

            // После работы с XML, если что-то было собрано в StringBuilder, записываем его в файл:
            String s = ss.toString().toUpperCase(); // Если нужно привести к верхнему регистру
            System.out.println(s);

            try (FileWriter w = new FileWriter("output.txt", false)) {
                w.write(s);
                w.append('\n');
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
