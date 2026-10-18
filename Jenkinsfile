pipeline {
    agent any

    environment {
        PATH = "C:\\Users\\venne\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin;${env.PATH}"
    }

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        IMAGE_NAME = 'student-complaint-system'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build and Test') {
            steps {
                bat 'mvn -B clean test package'
            }
        }
        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %IMAGE_NAME%:%BUILD_NUMBER% .'
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                bat 'kubectl apply -f k8s/'
                bat 'kubectl set image deployment/student-complaint-system student-complaint-system=%IMAGE_NAME%:%BUILD_NUMBER% --namespace=default'
                bat 'kubectl rollout status deployment/student-complaint-system --namespace=default --timeout=120s'
            }
        }
    }

    post {
        success {
            echo 'Build, tests, Docker image build, and Kubernetes deployment completed.'
        }
        failure {
            echo 'Pipeline failed. Review the stage logs above.'
        }
    }
}
