package ngavax.app;

import org.json.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class parseConfigTest {
    @Test void getPorts(){
        JSONObject obj = new JSONObject("{\"domains\":[{\"id\":\"example.com\",\"listen\":[80,443],\"locations\":[{\"directory\":\"/\",\"type\":\"static\",\"serve\":\"/var/www/html\"},{\"directory\":\"/api\",\"type\":\"proxy\",\"serve\":\"test.com:80\"}]}]}");
        parseConfig config = new parseConfig(obj);
        JSONArray ports = new JSONArray("[80,443]");
        assertEquals(ports.getInt(0), config.getPorts().get(0));
        assertEquals(ports.getInt(1), config.getPorts().get(1));
    }

    @Test void validateDomainConfig(){
        JSONObject obj = new JSONObject("{\"domains\":[{\"id\":\"example.com\",\"listen\":[80,443],\"locations\":[{\"directory\":\"/\",\"type\":\"static\",\"serve\":\"/var/www/html\"},{\"directory\":\"/api\",\"type\":\"proxy\",\"serve\":\"test.com:80\"}]}]}");
        parseConfig config = new parseConfig(obj);        JSONObject domain = new JSONObject("{\"id\":\"example.com\",\"listen\":[80,443],\"locations\":[{\"directory\":\"/\",\"type\":\"static\",\"serve\":\"/var/www/html\"}]}");
        assertEquals(domain.getString("id"), config.validateDomain("example.com").getString("id"));
        assertEquals(domain.getJSONArray("locations").getJSONObject(0).getString("type"), config.validateDomain("example.com").getJSONArray("locations").getJSONObject(0).getString("type"));
        assertEquals(domain.getJSONArray("locations").getJSONObject(0).getString("serve"), config.validateDomain("example.com").getJSONArray("locations").getJSONObject(0).getString("serve"));
        assertEquals(domain.getJSONArray("locations").getJSONObject(0).getString("directory"), config.validateDomain("example.com").getJSONArray("locations").getJSONObject(0).getString("directory"));
        assertEquals(null, config.validateDomain("notexample.com"));
    }
    /*
    I reworked this function, so the test needs to be refactored.
    @Test void validateDomainPort(){
        JSONObject obj = new JSONObject("{\"domains\":[{\"id\":\"example.com\",\"listen\":[80,443],\"locations\":[{\"directory\":\"/\",\"type\":\"static\",\"serve\":\"/var/www/html\"},{\"directory\":\"/api\",\"type\":\"proxy\",\"serve\":\"test.com:80\"}]}]}");
        parseConfig config = new parseConfig(obj);
        assertEquals(1, config.validateDomainPort("example.com", 80));
        assertEquals(1, config.validateDomainPort("example.com", 443));
        assertEquals(-1, config.validateDomainPort("example.com", 8080));
        assertEquals(-2, config.validateDomainPort("notexample.com", 80));
    }
    */

    @Test void getServices(){
        JSONObject obj = new JSONObject("{\"domains\":[{\"id\":\"example.com\",\"listen\":[80,443],\"locations\":[{\"directory\":\"/\",\"type\":\"static\",\"serve\":\"/var/www/html\"},{\"directory\":\"/api\",\"type\":\"proxy\",\"serve\":\"test.com:80\"}]}]}");
        parseConfig config = new parseConfig(obj);
        assertEquals(2, config.getServices("example.com").length());
        assertEquals("static", config.getServices("example.com").getJSONObject(0).getString("type"));
        assertEquals("/var/www/html", config.getServices("example.com").getJSONObject(0).getString("serve"));
        assertEquals("/", config.getServices("example.com").getJSONObject(0).getString("directory"));
    }

    @Test void validateAutoIndex(){
        JSONObject obj = new JSONObject("{\"domains\":[{\"id\":\"example.com\",\"listen\":[80,443],\"locations\":[{\"directory\":\"/\",\"type\":\"static\",\"serve\":\"/var/www/html\"},{\"directory\":\"/api\",\"type\":\"proxy\",\"serve\":\"test.com:80\"}]}]}");
        parseConfig config = new parseConfig(obj);
        assertEquals(false, config.validateAutoIndex(config.validateDirectory("example.com", "/")));
    }

    @Test void validateDirectory(){
        JSONObject obj = new JSONObject("{\"domains\":[{\"id\":\"example.com\",\"listen\":[80,443],\"locations\":[{\"directory\":\"/\",\"type\":\"static\",\"serve\":\"/var/www/html\"},{\"directory\":\"/api\",\"type\":\"proxy\",\"serve\":\"test.com:80\"}]}]}");
        parseConfig config = new parseConfig(obj);
        System.out.println(config.validateDirectory("example.com", "/api/test"));
        System.out.println(config.validateDirectory("example.com", "/ap/test"));
        assertEquals("/", config.validateDirectory("example.com", "/ap/test").getString("directory"));
        assertEquals("/api", config.validateDirectory("example.com", "/api/test").getString("directory"));
        assertEquals("/", config.validateDirectory("example.com", "/notapi").getString("directory"));
    }
}
