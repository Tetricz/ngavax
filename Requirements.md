# Ngavax requirements file

## Core functionality Goals

* [ ] Multi-threaded workload
* [ ] HTTP Proxy and caching server
* [ ] Serve static files and autoindex files

## Other Core functionality if we have the time

* [ ] TCP Load balancing
* [ ] SSL Verifcation

## Other possible goals

* [ ] Add SSL certificate generation

## Specifications/Details

### On Startup

* The application should read and parse the configuration file. On error output problem, then exit with code (-1).
* Check to make sure socket is available, if not, error and exit with code (-2).
* If the config file has no proxying needed, only load the request handler and Static/Autoindex handler.
* If only Proxying/Cache is needed, only load the proxy/cache handler.
* Multiple Proxy/Cache handlers can be loaded upon configuration.
* Load all handlers if they are needed
* Request Handler will always be loaded and will pass requests based on the HEADERS and URI.

### Multi-Threading

These are all their own threads.

* [ ] Request handler
* [ ] Response handler
* [ ] Proxy/Cache handler
* [ ] Static/Autoindex handler

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

### TCP Load balancing

TODO
