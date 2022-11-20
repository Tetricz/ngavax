package ngavax.app;

//import json.org
import org.json.*;
import java.util.*;

//This should be converted to a singleton, too bad I wrote it before I knew that.

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
            LOG.error(e);
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

    public JSONObject validateDomainPort(String id, int port){
        JSONObject domain = this.validateDomain(id);
        if(domain == null){
            return null;
        }
        JSONArray listen = domain.getJSONArray("listen");
        for(int i = 0; i < listen.length(); i++){
            if(listen.getInt(i) == port){
                return domain;
            }
        }
        return null;
    }

    public JSONArray getServices(String id){
        JSONObject domain = this.validateDomain(id);
        if(domain == null){
            return null;
        }
        return domain.getJSONArray("locations");
    }

    public boolean validateDirBlock(String id){
        JSONObject domain = this.validateDomain(id);

        if(domain.has("dirblock")){
            return domain.getBoolean("dirblock");
        }else{
            return true;
        }
    }

    private int getDefHelper(String id){
        JSONObject domain = this.validateDomain(id);
        return domain.getInt("default");
    }

    public JSONObject getDef(String id){
        JSONArray services = this.getServices(id);
        int key = this.getDefHelper(id);
        return services.getJSONObject(key);
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
            LOG.info("================Open=================");
            LOG.info("Domain: " + id);
            //LOG.info("    Listening on ports: " + getPorts());
            LOG.info("    Listening for directories:");
            for(int i = 0; i < getServices(id).length(); i++){
                JSONObject service = getServices(id).getJSONObject(i);
                //print a divider
                LOG.info("        -----------------------------");
                LOG.info("        Directory: " + service.getString("directory"));
                LOG.info("        Type: " + getType(service));
                LOG.info("        Serving: " + getServe(service));
                LOG.info("        Autoindex: " + validateAutoIndex(service));
            }
            LOG.info("================Close================");
        });
    }
}
