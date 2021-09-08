pipeline {
  stages{
      stage('Checkout'){
        git 'https://github.com/de4a-wp5/de4a-ssi-authority-agent-base'
      }

      stage('Test'){

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
        sh 'mvn package'
      }
    
  }
}
