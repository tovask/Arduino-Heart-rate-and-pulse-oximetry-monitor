
/*
 * https://www.arduino.cc/en/Reference/WiFiNINA
 */

#include "TCPServer.h"
#include "config.h"

void TCPServer::start() {
	LOG("Starting TCP Server...\n");
	server.begin();
}

bool TCPServer::send(String msg) {
	while (client && client.available()) {
		unsigned char buf[256];
		int len = client.read(buf, sizeof buf);
		buf[ (len<0) ? 0 : len ] = '\0';
		LOG(String("Received from TCP client: ")+(char*)buf+"\n");
	}
	if (!client || !client.connected()) {
		LOG("Searching for TCP client");
		int attempt = 0;
		while(!(client = server.available()) && attempt<10){
			LOG(".");
			delay(300);
			attempt++;
		}
		LOG("\n");
	}
	if (client) {
		LOG("Sending to TCP client: ");
		LOG(msg);
		// LOG("\n");
		// client.flush();
		client.print(msg);
		return true;
	} else {
		LOG("No TCP client\n");
		return false;
	}
}
