#ifndef TCPServer_h
#define TCPServer_h

#include <WiFiNINA.h>

class TCPServer {
private:
	
	WiFiServer server;
	WiFiClient client;
	
public:
	TCPServer() : server(23) {};
	void start();
	bool send(String msg);
};

#endif