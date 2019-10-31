# JenkinsPipelinePlugin

JenkinsPipelinePlugin is a Java based Jenkins Plugin for integrating Jenkins and JFrog Pipelines.

## Installation

Use the package manager [maven](https://maven.apache.org/) to install JenkinsPipelinePlugin.

```bash
mvn install
```



## Tests

### Unit tests

To run unit tests execute the following command: 
```
> mvn clean test
```




## Contributing
JFrog welcomes community contribution through pull requests.

For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.


## License
[MIT](https://choosealicense.com/licenses/mit/)


## Add JFrog Pipeline Support

### Add JFrog Pipeline token

![Alt text](docs/images/pipeline-token.png?raw=true "Title")




### Install Jenkins Plugin

![Alt text](docs/images/install-jenkins-plugin.png?raw=true "Title")




### Configure Pipeline Plugin

![Alt text](docs/images/jenkins-config.png?raw=true "Title")




### Add Support for Jenkins build 

![Alt text](docs/images/jenkins-job-config.png?raw=true "Title")



### Add Support for Jenkins Pipeline Scripted build 

```
node {
    def pipeline = Jfrog.newPipelineNotifier()
     try {
            sh 'exit 1'
        }
        catch (exc) {
            echo 'Something failed, I should sound the klaxons!'
            pipeline.sendJenkinsJob("FAILURE")
        }
}
```





### Add Support for Jenkins Pipeline Declarative build 

```
post {
        success {
            script {
                currentBuild.result = 'SUCCESS'
            }
            step([$class: 'NotifyPipelineServer'])
        }
}
```








### Connect to Jenkins Build from JFrog Pipeline

![Alt text](docs/images/jenkins-pipeline.png?raw=true "Title")
