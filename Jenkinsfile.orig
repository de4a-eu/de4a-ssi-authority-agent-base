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
<<<<<<< HEAD
                 sh 'mvn clean test sonar:sonar -Dsonar.host.url=$SONAR_URL -Dsonar.login=$SONAR_TOKEN'
=======
                sh 'mvn clean test sonar:sonar -Dsonar.host.url=$SONAR_URL -Dsonar.login=$SONAR_TOKEN'
>>>>>>> 93a2763700e8c1137c625aae9f9fcd79fa2a3687
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
<<<<<<< HEAD
            when {
                branch 'master'
=======
            when{
                branch 'main'
>>>>>>> 93a2763700e8c1137c625aae9f9fcd79fa2a3687
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
<<<<<<< HEAD
                  
                      def img
			    if (env.BRANCH_NAME == 'master') {
                      	env.VERSION = readMavenPom().getVersion()
                      	img = docker.build('de4a/de4a-ssi-authority-agent-base',".")
                      	docker.withRegistry('','docker-hub-token') {
                      		img.push('latest')
                      		img.push("${env.VERSION}")
                        }
                      }
                }
                sh 'docker system prune -f'
            }
        }
    stage('Docker iteration'){
            when {
                branch pattern: 'iteration\\d+', comparator: 'REGEXP'
            }
            agent { label 'master' }
            steps {
                script{
                    def img

                    env.VERSION = readMavenPom().getVersion()
                    img = docker.build('de4a/de4a-ssi-authority-agent-base','.')
                    docker.withRegistry('','docker-hub-token') {
                    	img.push("${env.BRANCH_NAME}")
                    	img.push("${env.VERSION}")
                    }
                    
                }
                sh 'docker system prune -f'
=======
                    def img
                    img = docker.build('de4a/de4a-ssi-authority-agent-base','--build-arg VERSION=$VERSION .')
                    docker.withRegistry('','docker-hub-token') {
                        img.push("${env.BRANCH_NAME}")
                        img.push('$VERSION')
                    }
                }
>>>>>>> 93a2763700e8c1137c625aae9f9fcd79fa2a3687
            }
        }
  }
}