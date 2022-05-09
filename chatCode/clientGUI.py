# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'client.ui'
#
# Created by: PyQt5 UI code generator 5.9.2
#
# WARNING! All changes made in this file will be lost!

from PyQt5 import QtCore, QtGui, QtWidgets

class Ui_Dialog(object):
    def setupUi(self, Dialog):
        Dialog.setObjectName("Dialog")
        Dialog.resize(400, 300)
        self.textEdit = QtWidgets.QTextEdit(Dialog)
        self.textEdit.setGeometry(QtCore.QRect(0, 10, 401, 221))
        self.textEdit.setReadOnly(True)
        self.textEdit.setObjectName("textEdit")
        self.message = QtWidgets.QLineEdit(Dialog)
        self.message.setGeometry(QtCore.QRect(0, 250, 341, 20))
        self.message.setObjectName("message")
        self.sendButt = QtWidgets.QPushButton(Dialog)
        self.sendButt.setGeometry(QtCore.QRect(350, 250, 51, 23))
        self.sendButt.setObjectName("sendButt")
        
        self.retranslateUi(Dialog)
        QtCore.QMetaObject.connectSlotsByName(Dialog)

    def retranslateUi(self, Dialog):
        _translate = QtCore.QCoreApplication.translate
        Dialog.setWindowTitle(_translate("Dialog", "Dialog"))
        self.sendButt.setText(_translate("Dialog", "Send"))


if __name__ == "__main__":
    import sys
    app = QtWidgets.QApplication(sys.argv)
    Dialog = QtWidgets.QDialog()
    ui = Ui_Dialog()
    ui.setupUi(Dialog)
    Dialog.show()
    sys.exit(app.exec_())

