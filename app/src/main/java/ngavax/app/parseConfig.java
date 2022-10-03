package ngavax.app;

//import json.org
import org.json.*;

import java.io.*;
import java.util.*;



class parseConfig {
    private JSONArray units;
    private String[] domains = new String[0];
    private HashMap<String, JSONArray> ports = new HashMap<String, JSONArray>();     //HashMap of domain to ports
    private HashMap<String, JSONArray> services = new HashMap<String, JSONArray>();  //HashMap of domain to services
    

    //Constructor for parseConfig
    //This will be ran once at the start of the program
    public parseConfig(String file) {
        try {
            FileReader file_config = new FileReader(file);
            JSONObject config = new JSONObject(new JSONTokener(file_config));

            this.units = config.getJSONArray("domains");

            this.units.forEach(domainNode -> {
                JSONObject domain = (JSONObject) domainNode;
                String id = domain.getString("id");
                this.domains = Arrays.copyOf(this.domains, this.domains.length + 1);
                this.ports.put(id, domain.getJSONArray("listen"));
                try {
                    this.services.put(id, domain.getJSONArray("locations"));
                } catch (Exception e) {
                    System.out.println(file + " does not have a locations array for " + id);
                    System.exit(-1);
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error reading file, check path");
            System.exit(-1);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error parsing config file, possible JSON syntax error");
            System.exit(-1);
        }
    }

    public int[] getPorts(String id){
        JSONArray ports = this.ports.get(id);
        int[] portsArray = new int[ports.length()];
        for(int i = 0; i < ports.length(); i++){
            portsArray[i] = ports.getInt(i);
        }
        return portsArray;
    }

    public JSONArray getServices(String id){
        return this.services.get(id);
    }

    public String[] getDomains(){
        return this.domains;
    }

    public void printConfig(){
        this.units.forEach(domainNode -> {
            String id = ((JSONObject) domainNode).getString("id");
            System.out.println("================Open=================");
            System.out.println("Domain: " + id);
            System.out.println("    Listening on ports: " + Arrays.toString(getPorts(id)));
            System.out.println("    Listening for directories:");
            for(int i = 0; i < getServices(id).length(); i++){
                JSONObject service = getServices(id).getJSONObject(i);
                //print a divider
                System.out.println("        -----------------------------");
                System.out.println("        Directory: " + service.getString("directory"));
                System.out.println("        Type: " + service.getString("type"));
                System.out.println("        Serving: " + service.getString("serve"));
            }
            System.out.println("================Close================");
        });
    }
}
