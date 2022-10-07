package ngavax.app;

//import json.org
import org.json.*;
import java.util.*;



class parseConfig {
    private JSONArray units;
    private int workerCount = 0;                                                     //Number of workers to spawn
    private HashMap<String, JSONArray> ports = new HashMap<String, JSONArray>();     //HashMap of domain to ports    
    private String root = new String();                                              //Root directory to search for files
    //private log_level: 0 = debug, 1 = info, 2 = warning, 3 = error, 4 = critical
    //private log_file: location and name of where to put log file

    //Constructor for parseConfig
    public parseConfig(JSONObject config) { //Move file reading to App.java instead and pass JSONObject here
        try {
            if(config.has("workers")){
                workerCount = config.getInt("workers");
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
                this.ports.put(id, domain.getJSONArray("listen"));
            });
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e);
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

    public JSONObject validateDomain(String id){
        for (Object domain : this.units) {
            if(((JSONObject)domain).getString("id").equals(id)){
                return (JSONObject)domain;
            }
        }
        return null;
    }

    public JSONArray getServices(String id){
        JSONObject domain = this.validateDomain(id);
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
            System.out.println("    Listening on ports: " + Arrays.toString(getPorts(id)));
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
