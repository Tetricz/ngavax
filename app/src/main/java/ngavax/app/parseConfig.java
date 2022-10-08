package ngavax.app;

//import json.org
import org.json.*;
import java.util.*;



class parseConfig {
    private JSONArray units;
    private int workerCount = 0;                                                           //Number of workers to spawn
    private JSONArray ports = new JSONArray();                                             //Array of all ports to listen on    
    private HashMap<String, JSONArray> domainToPorts = new HashMap<String, JSONArray>();   //HashMap of domain to ports
    private String root = new String();                                                    //Root directory to search for files
    //private log_level: 0 = debug, 1 = info, 2 = warning, 3 = error, 4 = critical
    //private log_file: location and name of where to put log file

    //Constructor for parseConfig
    public parseConfig(JSONObject config) { //Move file reading to App.java instead and pass JSONObject here
        try {
            if(config.has("workers")){
                this.workerCount = config.getInt("workers");
            }
            if(config.has("root_dir")){
                root = config.getString("root_dir");
            }
            if(!config.has("domains")){
                throw new JSONException("No key \"domains\" found in config file");
            }
            
            this.units = config.getJSONArray("domains");
            //Sets up domain
            this.units.forEach(domainNode -> {
                JSONObject domain = (JSONObject) domainNode;
                String id = domain.getString("id");
                JSONArray ports = domain.getJSONArray("listen");
                this.domainToPorts.put(id, ports);
                //check if this.ports already has the listen ports
                if(this.ports.length() > 0){
                    JSONArray listen = domain.getJSONArray("listen");
                    for(int i = 0; i < listen.length(); i++){
                        if(!this.ports.toList().contains(listen.getInt(i))){
                            this.ports.put(listen.getInt(i));
                        }
                    }
                }else{
                    JSONArray listen = domain.getJSONArray("listen");
                    this.ports = listen;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e);
            System.exit(-1);
        }
    }

    public int getWorkerCount(){
        return this.workerCount;
    }

    public String getRoot(){
        return this.root;
    }

    public JSONArray getPorts(){
        return this.ports;
    }

    public JSONObject validateDomain(String id){
        for (Object domain : this.units) {
            if(((JSONObject)domain).getString("id").equals(id)){
                return (JSONObject)domain;
            }
        }
        return null;
    }

    //If domain request on port is valid, return 1, else return -1
    public int validateDomainPort(String id, int port){
        JSONObject domain = this.validateDomain(id);
        if(domain == null){
            return -2;
        }
        JSONArray listen = domain.getJSONArray("listen");
        for(int i = 0; i < listen.length(); i++){
            if(listen.getInt(i) == port){
                return 1;
            }
        }
        return -1;
    }

    public JSONArray getServices(String id){
        JSONObject domain = this.validateDomain(id);
        if(domain == null){
            return null;
        }
        return domain.getJSONArray("locations");
    }

    //returns the location information if valid
    //returns null if invalid
    public JSONObject validateDirectory(String id, String directory){
        JSONArray services = this.getServices(id);
        for (Object service : services) {
            if(((JSONObject)service).getString("directory").equals(directory)){
                return (JSONObject)service;
            }
        }
        return null;
    }

    public String getType(JSONObject directory){
        return ((JSONObject)directory).getString("type");
    }

    public boolean validateAutoIndex(JSONObject directory){
        if(directory.has("autoindex")){
            return directory.getBoolean("autoindex");
        }else{
            return false;
        }
    }

    public String getServe(JSONObject directory){
        return ((JSONObject)directory).getString("serve");
    }

    public void printConfig(){
        this.units.forEach(domainNode -> {
            String id = ((JSONObject) domainNode).getString("id");
            System.out.println("================Open=================");
            System.out.println("Domain: " + id);
            //System.out.println("    Listening on ports: " + getPorts());
            System.out.println("    Listening for directories:");
            for(int i = 0; i < getServices(id).length(); i++){
                JSONObject service = getServices(id).getJSONObject(i);
                //print a divider
                System.out.println("        -----------------------------");
                System.out.println("        Directory: " + service.getString("directory"));
                System.out.println("        Type: " + getType(service));
                System.out.println("        Serving: " + getServe(service));
                System.out.println("        Autoindex: " + validateAutoIndex(service));
            }
            System.out.println("================Close================");
        });
    }
}
