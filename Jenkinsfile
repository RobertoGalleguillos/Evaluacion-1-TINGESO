pipeline{
    agent any
    tools{
        maven "maven"
    }
    stages{
        stage("Build JAR FILE"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'tingesoo1', url: 'https://github.com/RobertoGalleguillos/Evaluacion-1-TINGESO']])
                bat "mvn clean install"
            }
        }
        stage("Test"){
            steps{
                bat "mvn test"
            }
        }
        stage("SonarQube Analysis"){
            steps{
                bat "mvn clean verify sonar:sonar -Dsonar.projectKey=evaluacion1 -Dsonar.projectName='evaluacion1' -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_5d794c994b30fa6ac5efd993352dccff6553419c"
            }
        }
        stage("Build Docker Image"){
            steps{
                bat "docker build -t robertogalleguillos/evaluacion1_docker ."
            }
        }
        stage("Push Docker Image"){
            steps{
                withCredentials([string(credentialsId: 'dckrhubpassword', variable: 'dckpass')]) {
                    bat "docker login -u robertogalleguillos -p ${dckpass}"
                }
                bat "docker push robertogalleguillos/evaluacion1_docker"
            }
        }
    }
    post{
        always{
            bat "docker logout"
        }
    }
}