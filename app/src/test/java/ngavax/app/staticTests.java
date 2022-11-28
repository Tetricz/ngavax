package ngavax.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class staticTests {
    private staticHandler zap = new staticHandler();
//
//    @Test void indexTest(){
//        String actual = zap.indexPath("/");
//        String expected = """
//<html>
//<head><title>Index</title></head>
//<body>
//<h1>Index</h1><hr><pre>
//<a href=\"../\">../</a>
//<a href=\"/bin/\">bin</a>
//<a href=\"/build/\">build</a>
//<a href=\"/src/\">src</a>
//<a href=\"/build.gradle.kts\">build.gradle.kts</a>
//<a href=\"/config_example.json\">config_example.json</a>
//</pre><hr></body>
//</html>""";
//        assertEquals(expected, actual);
//    }

    @Test void testFileTest(){
        String path = "C:/github/ngavax/www/";
        byte[] actual = zap.testFile(path);
        for(int i=0; i < actual.length; i++){
            System.out.print((char)actual[i]);
        }
    }
}
