package ngavax.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class staticTests {
    private staticHandler zap = new staticHandler(".");

    @Test void indexTest(){
        String actual = zap.indexPath("/");
        String expected = """
<html>
<head><title>Index</title></head>
<body>
<h1>Index</h1><hr><pre>
<a href=\"../\">../</a>
<a href=\"/bin/\">bin</a>
<a href=\"/build/\">build</a>
<a href=\"/src/\">src</a>
<a href=\"/build.gradle.kts\">build.gradle.kts</a>
<a href=\"/config_example.json\">config_example.json</a>
</pre><hr></body>
</html>""";
        assertEquals(expected, actual);
    }
}
