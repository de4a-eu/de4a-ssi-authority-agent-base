pipeline {
  agent none
  
  stages{
    
      stage('Test') {
            when {
                anyOf {
                    branch 'development'; branch pattern: 'PR-\\d+', comparator: 'REGEXP'
                }
            }
            agent {
                docker {
                    image 'maven:3-adoptopenjdk-11'
                    args '-v $HOME/.m2:/root/.m2 -e HOME="." --network docker-ci_default'
                }
            }
            steps {
                sh 'mvn clean test sonar:sonar -Dsonar.host.url=$SONAR_URL -Dsonar.login=$SONAR_TOKEN'
            }
        }
    
      stage('Build'){
         when {
                anyOf{
                    branch 'main'; branch pattern: 'iteration\\d+', comparator: 'REGEXP'
                }
            }
            agent {
                docker {
                    image 'maven:3-adoptopenjdk-11'
                    args '-v $HOME/.m2:/root/.m2 -e HOME="." --network docker-ci_default'
                }
            }
            steps {
                sh 'mvn clean package'
            }
      }
    
      stage('Docker'){
            when{
                branch 'main'
            }
            agent { label 'master' }
            environment {
                VERSION=readMavenPom().getVersion()
            }
            steps {
                script{
                    def img
                    if (env.BRANCH_NAME == 'main') {
                        img = docker.build('de4a/de4a-ssi-authority-agent-base','--build-arg VERSION=$VERSION .')
                        docker.withRegistry('','docker-hub-token') {
                            img.push('latest')
                            img.push('$VERSION')
                        }
                    }
                }
            }
        }
	stage('Docker iterations') {
            when{
                branch pattern: 'iteration\\d+', comparator: 'REGEXP'
            }
            agent { label 'master' }
            environment {
                VERSION=readMavenPom().getVersion()
            }
            steps {
                script{
                    def img
                    img = docker.build('de4a/de4a-ssi-authority-agent-base','--build-arg VERSION=$VERSION .')
                    docker.withRegistry('','docker-hub-token') {
                        img.push("${env.BRANCH_NAME}")
                        img.push('$VERSION')
                    }
                }
            }
        }
    
  }
}