# from https://realpython.com/python-send-email/

import smtplib, ssl
import getpass

port = 465  # For SSL
user = input("Type your gmail username and press enter: ")
password = getpass.getpass(prompt="Type your password and press enter: ")
receiver_email = input("Type the receiver email and press enter: ")

print("Using SMTP_SSL")

# Create a secure SSL context
context = ssl.create_default_context()

sender_email = "%s@gmail.com" % user
message = """\
Subject: Hi there

This message is sent from Python using SMTP_SSL."""

with smtplib.SMTP_SSL("smtp.gmail.com", port, context=context) as server:
    server.login(sender_email, password)
    server.sendmail(sender_email, receiver_email, message)

message = """\
Subject: Hi there

This message is sent from Python using .starttls()."""

# another way to send email
print("Using .starttls()")
import smtplib, ssl

smtp_server = "smtp.gmail.com"
port = 587  # For starttls

# Create a secure SSL context
context = ssl.create_default_context()

# Try to log in to server and send email
try:
    server = smtplib.SMTP(smtp_server,port)
    server.ehlo() # Can be omitted
    server.starttls(context=context) # Secure the connection
    server.ehlo() # Can be omitted
    server.login(sender_email, password)
    server.sendmail(sender_email, receiver_email, message)
except Exception as e:
    # Print any error messages to stdout
    print(e)
finally:
    server.quit()

