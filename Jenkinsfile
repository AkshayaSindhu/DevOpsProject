pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        PATH = "C:\\Users\\venne\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Build Application') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker --version'
                bat 'docker build -t student-complaint-system:%BUILD_NUMBER% .'
            }
        }

        stage('Verify Docker Image') {
            steps {
                bat 'docker images student-complaint-system'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the Console Output for details.'
        }
    }
}