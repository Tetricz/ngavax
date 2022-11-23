# Python 3 server example
from http.server import BaseHTTPRequestHandler, HTTPServer
import time

hostName = "localhost"
serverPort = 8080

class MyServer(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        self.wfile.write(bytes("<html><head><title>Sample Website</title><style>\
        body {\
        font-family: Arial, Helvetica, sans-serif;\
        margin: 0;\
        }\
        .header {\
        padding: 80px;\
        text-align: center;\
        background: #1abc9c;\
        color: white;\
        }\
        .header h1 {\
        font-size: 40px;\
        }\
        </style>\
        </head>", "utf-8"))
        self.wfile.write(bytes("<p>Request: %s</p>" % self.path, "utf-8"))
        self.wfile.write(bytes("<body>", "utf-8"))
        self.wfile.write(bytes("<div class=\"header\">", "utf-8"))
        self.wfile.write(bytes("<h1>My Sample Web Site</h1>", "utf-8"))
        self.wfile.write(bytes("<p>This is an example web server.</p></div>", "utf-8"))
        self.wfile.write(bytes("</body></html>", "utf-8"))

if __name__ == "__main__":        
    webServer = HTTPServer((hostName, serverPort), MyServer)
    print("Server started http://%s:%s" % (hostName, serverPort))

    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("Server stopped.")