# APM-Metric-Smoketest
The idea of this project is to provide a lightweight and easy to use 
framework to regularly check that defined metrics are available and 
environment is setup correctly. Currently, the projects supports smoke 
testing environments monitored with Computer Associate Wily Introscope 
APM. Nevertheless, the framework is designed to support several APM 
tools. 

**Features:**
- Easy extensible with new functionality  
- HTML test report (Powered by ReportNG) 
- POJO test statistics report
- Support for custom reports 
- Supports configuration by CLI 
- Supports configuration by YAML files 

## How to use it

- The APM-Metric-Smoketest artifact is not yet available on any 
maven repository. You have to clone the repository and build it 
yourself. 

- Since CA Wily Introscope is a commercial tool, those artifacts must be 
added to the classpath. 

**Examples**

An exemplary CA Wily Introscope smoke test is provided under 
*src/test/java*. 


### Requirements

-   JDK 8 

-   Maven

-  CA Wily Introscope artifacts 

### Contribute

Just fork.
