# connectionPoolingDemo

A demonstration of what connection pooling issues can look like while using the Digma plugin on Intellij.

## Setup

First run the python script to both initialize and populate the sample database

Then run the project using one of the Intellij configurations, DemoAppMvn will clean install before running, DemoApp simply runs

Finally, load the server using the loadScript.sh to add concurrent load and observe findings.

## Making Changes

In order to observe the effect a difference in pool sizes presents, change the property `spring.datasource.hikari.maximum-pool-size` to a different number in `applications.properties`.
