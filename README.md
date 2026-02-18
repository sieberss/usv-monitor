# USV Monitor
My Capstone Project of the Java Fullstack Developer Bootcamp at NeueFische. Relaunch of a legacy software with modern technologies.

## What the Application does
It manages UPSes (Uninterruptible Power Supplies) and Servers connected to them. In monitoring mode the power and battery status of the UPSes is checked every 5 seconds. In case of Power-off the admin is alerted by change of colour and the remaining battery time is displayed. When this  battery time falls below the defined limit, servers connected to that UPS are shut down.

## Backend
* Java 21
* Spring Boot 3.3.4
* Mongo DB
* Spring Security (Basic Auth)
* Lombok
* JUnit + Mockito
## Frontend
* React 18.3.1
* TypeScript 
* Vite 
* Axios 
* react-router-dom
## Deployment
* CI/CD with GitHub Actions
* Docker image: https://hub.docker.com/r/sieberss/usv-monitor
* Demo site: https://usv-monitor-latest.onrender.com/
* (Attention: It takes about 1 minute to start container! Use password "adminpass")

## Features
* CRUD operations for UPSes, Servers and Credentials
* Servers are assigned a UPS that powers it and credentials for sending a shutdown command
* Content only visible after login
* Currently designed for only one admin user, therefore no username
* User is created when first accessing the site
* Two modes: Configuration and Monitoring
* In Monitoring mode UPS status request sent to backend every 5 seconds
* Display of current status in frontend
* In case of Power Off on UPS: alert by changing background color of UPS and associated servers
* Shutdown of Servers indicated by background color
* UPS status is currently simulated by randomly provided Power-Off events
* Will be replaced by SNMP requests later

## Project background
In a previous job I had to modify a legacy Java desktop software ("UMSy Shutdown Manager") according the needs of a new major client. 
It was very hard to maintain (Big Ball of Mud) and implemented outdated technologies, so I would have preferred to write a completely new version.

In the BootCamp at NeueFische I had the opportunity to practise modern technologies of the Java Ecosystem. In a 4 week capstone project all participants had to show what they learned before. As working on UMSy had been painful I decided to relaunch it as a maintainable web application.

## Future Development
Unfortunately the company and its client have decided to abandon Java, so there is currently no need to continue development. 

If your company is interested in completing the application or you want to collaborate with me on it, I am looking forward for a message from you.
