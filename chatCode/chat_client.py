# chat_client.py

import sys
import socket
import select
 
def chat_client():
    if(len(sys.argv) < 3) :
        print ('Usage : python chat_client.py hostname port')
        sys.exit()

    host = sys.argv[1]
    port = int(sys.argv[2])
     
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(5)
     
    # connect to remote host
    try :
        s.connect((host, port))
        #print(s)
    except :
        print ('Unable to connect')
        sys.exit()

    t = socket.socket()
    #t.connect((host,port))
    print ('Connected to remote host. You can start sending messages')
    sys.stdout.write('[Me] '); sys.stdout.flush()
     
    while 1:
        #print(s)
        socket_list = [t, s]
        #print(socket_list)
        # Get the list sockets which are readable
        #ready_to_read,ready_to_write,in_error = select.select(socket_list, [], []) 
        # ready_to_read.append(s)
        #print(ready_to_read)
        ready_to_read = socket_list
        for sock in ready_to_read:             
            if sock == s:
                # incoming message from remote server, s
                data = sock.recv(4096)
                if not data :
                    print ('\nDisconnected from chat server')
                    sys.exit()
                else :
                    #print data
                    sys.stdout.write(data.decode('utf-8'))
                    sys.stdout.write('[Me] '); sys.stdout.flush()     
        
            else :
                # user entered a message
                msg = sys.stdin.readline()
                s.send(msg.encode("utf-8"))
                sys.stdout.write('[Me] '); sys.stdout.flush()
            #sock.close()
        #break
    s.close()

if __name__ == "__main__":

    sys.exit(chat_client())
