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
                        branch 'master'; branch pattern: 'iteration\\d+', comparator: 'REGEXP'
                    }
                }
                agent {
                    docker {
                        image 'maven:3-adoptopenjdk-11'
                        args '-v $HOME/.m2:/root/.m2 --network docker-ci_default'
                    }
                }
                steps {
                    sh 'mvn clean package -U'
                }
      }
    
      stage('Docker'){
            
            agent { label 'master' }
            steps {
                script{
                  
                      def img
                      env.VERSION = readMavenPom().getVersion()
                      img = docker.build('de4a/de4a-ssi-authority-agent-base',".")
                      docker.withRegistry('','docker-hub-token') {
                      img.push('latest')
                      img.push("${env.VERSION}")
                          }
                      }
                
                sh 'docker system prune -f'
            }
        }
    
  }
}