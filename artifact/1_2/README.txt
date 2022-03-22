Site Updater
_______________________

This is a utility I use to automatically add an update to the feed of updates on my website, www.npwd.tech. It buffers the update.html page into a List of lines of the html and overwrites it with an edited buffer (current page + new update entry).

Very simple project, serves my systems. Feel free to look at it.

v1_2
_____________

**Added FTP capability
  -Since the update page is now on a remote filesystem from my current development environment, it uses FTP to create a "filebuffer"
    *Does the same thing with the addition of shuffling the filebuffer on and off my local filesystem AND to the server