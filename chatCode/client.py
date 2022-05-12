import sys
import socket
import select
import msvcrt
from PyQt5.QtWidgets import *
from PyQt5 import uic
from PyQt5 import QtCore
import threading

client_ui = uic.loadUiType("client.ui")[0]  
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
ready_to_read = []
class clientGui(QWidget, client_ui):
    def __init__(self):
        super().__init__()
        self.setupUi(self)
        self.setWindowTitle("Client")

    def onsendcl(self):
        msg = str(self.sendText.text() + '\n')
        if(msg != "\n"):
            s.send(msg.encode('utf-8'))
            self.sendAndRec.append('[ME]: ' + msg)
            self.sendText.setText("")
    
    def append(self):
        ready_to_read.append(sys.stdin)
        print(ready_to_read)

def clientStart():
        host = 'localhost' #sys.argv[1]
        port = 9009 #int(sys.argv[2])

        s.settimeout(5)
        
        # connect to remote host
        try :
            s.connect((host, port))
        except :
            print ('Unable to connect')
            sys.exit()

        widget.sendAndRec.append('Connected to remote host. You can start sending messages')

        while 1:
            ready_to_read = select.select([s], [], [], 1)[0]
            #widget.sendText.textChanged.connect(widget.append)
            #if msvcrt.kbhit(): 
            ready_to_read.append(sys.stdin)
            #print(ready_to_read)
            for sock in ready_to_read:             
                if sock == s:
                    # incoming message from remote server, s
                    data = sock.recv(4096)
                    if not data :
                        print ('\nDisconnected from chat server')
                        sys.exit()
                    else :
                        #print data
                        widget.sendAndRec.append(data.decode('utf-8')) 
            
                else :
                    # user entered a message
                    widget.sendButt.clicked.connect(widget.onsendcl)
                    sys.stdout.flush()
        s.close()

if __name__ == "__main__":
    app = QApplication(sys.argv)
    widget = clientGui()
    widget.show()
    t = threading.Thread(target= clientStart, args=())
    t.start()
    sys.exit(app.exec())
    t._stop()