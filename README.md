Sample Spring Boot project using the [Spring Cloud - Cloud Foundry Service Broker](https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker).

# Overview

This sample project uses the Spring Cloud - Cloud Foundry Service Broker to implement a HawQDB service. The HawQDB service also uses itself to persist service instances and bindings.

## Getting Started

You need to install and run HawQDB somewhere and configure connectivity MASTER_JDBC_URL in [manifest.yml](manifest.yml).

For simplicity, I have created an Amazon Image that you could start off with. This AMI contains several other Pivotal Big Data Suite components as well such as - SpringXD, Spark, Pivotal Hadoop, Pivotal HAWQ and Gemfire. There is no .pem requirement to login into the EC2 instance once AMI is launced.
  
Log in to your AWS account and search for "Name" attribute with value "PCF Microservice" (available in Singapore region - ami-7d5c9c1e, US East (N. Virginia) - ami-9d1f51f7 and US West (N. California) - ami-84afc7e4)
      
- Launch it and make sure you choose t2.large (8GB) instance size.
          
- Auto assign a public IP address
              
- Add 25 GB SSD storage
                  
- Keep all traffic open on all ports in your security group settings
                      
- No need to choose a key pair.
                          
Once launched, you could ssh to machine using gpadmin/changeme credentials.

Start HAWQ using this [guide](start_phd.pdf)

## Build it:

    ./gradlew build


## Push to cloud foundry

    cf push 


## Create service broker

    Look into logs of your app (ie run "cf logs hawqdb-broker --recent") and look for this entry 

    OUT Using default security password: c424e1ef-f6d3-4deb-bb94-16271a3db58f

    copy this password and run following script to create the broker and enable plans in marketplace

    Note: you might have to change the hawdb-broker application url below as it could be different in your set up

    cf create-service-broker hawqsb user c424e1ef-f6d3-4deb-bb94-16271a3db58f http://hawqdb-broker.local.micropcf.io

    cf enable-service-access hawqdatabase

## Access it in Markeplace

    Run "cf marketplace" to see that your HAWQ database related plans are available

## Create hawq db Service

    Run "cf create-service hawqdatabase basic myhawqdb" to create a new database on demand

    Run "cf bind-service <appname> myhawqdb" to bind it to any application. When you run "cf app <appname>" you could see hawq credentials to access the newly created hawq database.

## Unbind and delete hawq db

   Run "cf unbind-service <appname> myhawqdb" to unbind hawq db.

   Note - Before deleting the service, make sure you are not connected to hawq db. I've not coded for extra conditions in my code. 

   Run "cf delete-service myhawqdb -f" to delete hawq database.


