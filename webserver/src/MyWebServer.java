/*Stephen Bromfield
 * MyWebServer.java
 */
import java.io.*;
import java.net.*;
import java.util.*;

public final class MyWebServer {

    // The default port number for incoming requests.
    static int serverPort = 6789;

    // The main function.
    public static void main(String[] args) throws Exception {
        // Parse command-line and see if a port number is given;
        // otherwise use default.
        if(args.length == 1) serverPort = Integer.parseInt(args[0]);
        else if(args.length != 0) {
            System.err.println("Usage: java MyWebServer [server_port_number]");
            System.exit(1);
        }

        // Establish the listen socket.
        ServerSocket listenSocket = new ServerSocket(serverPort);
	System.out.println("[] Open server socket: port=" + serverPort);

	// Process HTTP service requests in an infinite loop.
	while(true) {
	    // Wait for an incoming connection from client; set up the
	    // streams for communication with the client.
	    Socket connectionSocket = listenSocket.accept();

	    // Construct an object to process the HTTP request message.
	    HttpRequest request = new HttpRequest(connectionSocket);

	    // Create a new thread to process the request.
	    Thread thread = new Thread(request);

	    // Start the thread from the HttpRequest.run() method.
	    thread.start();
	}
    }
}

final class HttpRequest implements Runnable {
    // The socket represents an incoming client connection.
    Socket connectionSocket;

    // Constructor.
    public HttpRequest(Socket socket) throws Exception {
	connectionSocket = socket;
    }

    // Implement the run() method of the Runnable interface.
    public void run() {
	try {
	    processRequest();
	} catch (Exception e) {
	    System.err.println(e);
	}
    }

    private void processRequest() throws Exception {
	// Get a reference to the socket's input and output streams.
	BufferedReader inFromClient =
	    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient =
	    new DataOutputStream(connectionSocket.getOutputStream());

        String requestMessageLine = inFromClient.readLine();
        String ext = getExtension(requestMessageLine);
        String htmlBody = null;
	// Display the request line.
	System.out.println(requestMessageLine);

	// Display the header lines.
	String headerLine = null;
	while ((headerLine = inFromClient.readLine()).length() != 0) {
	    System.out.println(headerLine);
	}
        //Test to see if the request is not a GET method
        if(!requestMessageLine.substring(0, 3).contains("GET"))
        {
	 htmlBody = "<HTML>" +
	    "<HEAD><TITLE>501 Not Done</TITLE></HEAD>" +
	    "<BODY>Error 501 Not Implemented</BODY></HTML>";
	outToClient.writeBytes("HTTP/1.0 200 OK\r\n");
	outToClient.writeBytes("Content-Type: text/html \r\n");
	outToClient.writeBytes("Content-Length: " + htmlBody.length() + "\r\n");
	outToClient.writeBytes("\r\n");
	outToClient.write(htmlBody.getBytes(), 0, htmlBody.length());
        }
        FileInputStream fis = null;
        boolean fileExists = true;
        String file = getPath(requestMessageLine);

        try
        {
            fis = new FileInputStream("." +getPath(requestMessageLine).trim());
        }catch(FileNotFoundException e)
        {
            fileExists = false;
        }
            System.out.println(getPath(requestMessageLine));
        String statusLine = null;
        String contentTypeLine =  null;

        if(fileExists)
        {
            statusLine = "HTTP/1.0 200 OK\r\n";
            contentTypeLine = "Content-Type: " + contentType(ext) + "\r\n";
        }else
        {
            //houston, we have a problem
            htmlBody = "<HTML>" +
            "<HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
	    "<BODY>Error 404 Not Found</BODY></HTML>";
            statusLine ="HTTP/1.0 404 Not Found\r\n";
            contentTypeLine = "Content-Type: text/html \r\n";
        }
        outToClient.writeBytes(statusLine);
        outToClient.writeBytes(contentTypeLine);
        outToClient.writeBytes("\r\n");

        if(fileExists)
        {
            sendBytes(fis, outToClient);
            fis.close();

        }else
        {
            outToClient.writeBytes(htmlBody);
        }

	inFromClient.close();
	outToClient.close();
	connectionSocket.close();
    }

    private static String contentType( String fileName)
    {
        
        if(fileName.contains(".htm") || fileName.contains(".html"))
        {
            return "text/html";
        }else if(fileName.contains(".gif"))
        {
            return "image/gif";
        }else if(fileName.contains(".jpg") || fileName.contains(".jpeg")
                || fileName.contains(".jpe"))
        {
            return "image/jpeg";
        }else if(fileName.contains(".pdf"))
        {
            return "application/pdf";
        }else
        {
            return "application/octet-stream";
        }
    }
    //Gives me the file extension
    private static String getExtension( String request)
    {
        int end = request.indexOf("HTTP");
        int start = request.indexOf(".");
        //in case we have some f'ed up situation
        if(end == 0 || start > end)
        {
            return " ";
        }
        
        return request.substring(start, end);
    }
    //sends file to client
    private static void sendBytes( FileInputStream fis, OutputStream os) throws
            Exception
    {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while((bytes = fis.read(buffer)) != -1)
        {
            os.write(buffer, 0, bytes);
        }
    }
    private static String getPath( String request)
    {
        int end = request.indexOf("HTTP");

        //in case we have some f'ed up situation
        if(end == 0 || 4 >= end)
        {
            return " ";
        }

        return request.substring(4, end);
    }
}