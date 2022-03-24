
# v1_2


**Added FTP Capability**
  - The update page on **www.npwd.tech** is now on a remote filesystem from my current development environment. SiteUpdater was using local file operations to add the inputted update entry to the page that used to exist locally, but it now uses FTP to overwrite `updates.html`remotely.
