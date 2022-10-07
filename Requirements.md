# Ngavax requirements file

## Core functionality Goals

* [ ] Multi-threaded workload
* [ ] HTTP Proxy
* [ ] Serve static files and autoindex files

## Other Core functionality if we have the time

* [ ] TCP Load balancing
* [ ] SSL Verifcation
* [ ] Caching(inside proxy handler)

## Other possible goals

* [ ] Add SSL certificate generation

## Specifications/Details

### On Startup

* The application should read and parse the configuration file. On error output problem, then exit with code (-1).
* Check to make sure socket is available, if not, error and exit with code (-2).
* load worker threads

### Multi-Threading

These are all on their own threads.

* [ ] Request handler(always listening)
* [ ] Worker threads(spawn based on config)

Have a warning if the number of workers threads is more than the number of cores.

The worker threads will be the:

* [ ] Proxy/Cache handler (spawns based on config)
* [ ] Static/Autoindex handler (spawn based on config)

How we choose which worker to use is simple. A least recently used queue will be used to determine which worker to use. This will be done in the request handler.

Why we aren't doing it the same as Nginx. The way Nginx handles multiprocessing is by starting with the Main process which reads the config and open the listeners on the ports. This is the only process that does this as it is "priviledged". Then it will spawn a cache loader. This loads the cache on the disk into memory and then dies. A cache Handler is also spawned shortly after which handles the pruning and ensures the cache doesn't grow too large. Finally the worker processes which handle just about everything else spawn. The amount spawned are based on the configuration, but by default (auto) it spawns one process per core of your machine. The workers will read and write to the disk and communicate with upstream servers.

The problem with doing this in Java is the JVM. While Java supports multithreading natively, it does not support multiprocessing. Thus we use this same concept, but with multi-threading instead. The main thread will spawn the worker threads and whenever it gets a request it will pass it to the worker threads. There will be event queueing for the workers, so hopefully we can balance that out a bit.

TLDR - Java is not C and multiprocessing isn't as efficient. Also I don't wanna

### Request Handler

Listens on the socket and accepts connections. Should read the HEADERS if available and pass the request to the appropriate handler.
Ports can be specified in the config file.

### Response Handler

Sends response back to the client. Modify's HEADERS if needed. (I think this handles certificate verification as well)
Ports to send response (I think) are in the HEADERS.

### Proxy and caching

For the time being this will be limited to one thread, but could possibly be scaled to multiple threads.

This thread handles proxy requests and caches based on rules set in the configuration file. The request handler reads the HEADERS and sends the request to the proxy/cache handler. The proxy/cache handler will then check the cache for the request, if cache miss, it will send the request to the server and cache the response. If cache hit, it will send the cached response to the request handler. Cache will be retired based on LRU(Least Recently Used) rules.

Proxying will be pretty simple, modify the headers if needed and send the request to the proxied server. If response timeouts, it will return a 504 error to the client. If the proxied server responds with an error message, display the error to the client. The error can be specified as a statically/dynamically generated served file or to let the proxied server handle the error.

### Serve static files and autoindex files

File path will be specified in the configuration file. If the file path is a directory, it will serve the index.html file if it exists. If the file path is a file, it will serve the file. If the file path is not found, it will return a 404 error to the client.

Autoindexing will be done by having a generated html file with all the files in a specified directory. The index will be cached and updated based on rules set in the configuration file. Example rules: on a cron schedule, on a request(with limits on how often), on a file change(if possible), on start.

### Load balancing

Methods for load balancing.

* Round Robin
* Least connections
* Fastest response

TODO create config

### Config File

Default file is a server.json file in the same directory as the executable. The config file will be in JSON format. The config file will be read and parsed on startup. If the config file is not found, the application will exit with code (-1). If the config file is not in JSON format, the application will exit with code (-1).

Example:

```json
    "domains": [
        {
            "id": "4353.uh.edu",
            "listen": [
                80,
                443
            ],
            //This is an example of a way for us to setup user variables
            //Only do this if we have some extra time
            "env_vars": [
                "SOME_VAR=80",
                "SOME_OTHER_VAR=bar"
            ],
            //locations is a keyword to listen for directory requests. "/" listens for index.html inside the html directory, then allow any other files to be accessed.
            //If a file does not exist, it will then server a 404.html if it exists or just 404
            "locations": [
                {
                    "directory": "/",
                    "type": "proxy",
                    "serve": "http://localserver.tld:80"
                }
            ]
        },
        {
            "id": "banana.4353.uh.edu",
            "listen": [
                80,
                443
            ],
            "locations": [
                {
                    "directory": "/banana",
                    "type": "static",
                    "serve": "/home/ngavax/html/banana.html"
                }
            ]
        },
        {
            "id": "*.4353.uh.edu",
            "listen": [
                80,
                443
            ],
            "locations": [
                {
                    "directory": "/",
                    "type": "static",
                    "serve": "/home/uh.edu/html/404.html"
                }
            ]
        },
        {
            "id": "uhhh.edu",
            "listen": [
                80,
                443
            ],
            "locations": [
                {
                    "directory": "/",
                    "type": "static",
                    "serve": "/home/uh.edu/html/"
                },
                {
                    "directory": "/ftp",
                    "type": "static",
                    "serve": "/home/uh.edu/ftp/",
                    "autoindex": true
                },
                {
                    "directory": "/pass",
                    "type": "proxy",
                    "serve": "192.168.1.55:80"
                }
            ]
        }
    ]
}
```
