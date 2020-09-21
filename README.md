CONTENTS OF THIS FILE
---------------------

 * Introduction
 * Requirements
 * Installation
 * Configuration
 * Maintainers


INTRODUCTION
------------
Project using appium and cucumber lib in automation test android app. Other, using socket.io and unirest library to check response from server.

REQUIREMENTS
------------

1. Install Maven lib.
2. Install java 8 or other more 8.
3. Install Appium tools.

INSTALLATION
------------

1. Install Maven lib: https://howtodoinjava.com/maven/how-to-install-maven-on-windows/
2. Install java 8 or other more 8. https://www.java.com/en/download/help/windows_manual_download.xml?printFriendly=true
3. Install Appium tools. http://appium.io/


CONFIGURATION
-------------
1. Install project
- run terminal: mvn test
 
2. Debug
- Info: resources/log4j2.properties
- logs/app.log
 
3. env
- Info: resources/config.properties
 
4. Step start project test
1. Run appium tool
- Config: advanced/ allow session override: true

2. Run test
- config: src\test\java\AVISANDROID\RunCucumberTest

MAINTAINERS
-----------
