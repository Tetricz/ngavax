# parseConfig Documentation

## Constructor

Takes in a JSONObject. Then the contrsutor will read the file into the object.

Exmaple

```java
JSONObject config = new JSONObject();
parseConfig parsed = new parseConfig(config);
```

## Methods

### getWorkerCount()

Returns an integer representing the number of workers specified in the config file.

### getPorts()

Returns a JSONArray containing all the ports that need to be opened.

### validateDomain(String id)

Takes in a string representing the host name(domain) and returns the JSONObject representing the domain.

If the domain is invalid, it will return null.

### validateDomainPort(String id, int port)

Takes in a string representing the host name(domain) and an integer representing the port number.
Then check to make sure the port is valid for the domain.

If valid, returns 1. If invalid, returns -1.
If domain is invalid, returns -2.

### getServices(String id)

Takes in a string representing the host name(domain) and returns a JSONArray containing all the services for that domain.

### getType(JSONObject directory)

Takes in a JSONObject representing a service and returns a string representing the type of service.

### getServe(JSONObject directory)

Takes in a JSONObject representing a service and returns a string representing the path to the file to be served.

### validateAutoIndex(JSONObject directory)

Takes in a JSONObject representing a service and returns a boolean representing whether or not autoindex is enabled.

### validateDirectory(String id, String directory)

Takes in a string representing the host name(domain) and a string representing the path to the directory.
Then this will check to make sure the directory is valid for the domain.
