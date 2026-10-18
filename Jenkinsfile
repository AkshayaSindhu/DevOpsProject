
pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        PATH = "C:\\Users\\venne\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin;${env.PATH}"
        KUBECONFIG = 'C:\\ProgramData\\Jenkins\\.kube\\config'
        APP_NAME = 'student-complaint-system'
        K8S_DEPLOYMENT = 'student-complaint-system'
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
                bat 'docker build -t %APP_NAME%:%BUILD_NUMBER% .'
            }
        }

        stage('Verify Docker Image') {
            steps {
                bat 'docker images %APP_NAME%'
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat 'kubectl config current-context'
                bat 'kubectl get nodes'
                bat 'kubectl set image deployment/%K8S_DEPLOYMENT% %APP_NAME%=%APP_NAME%:%BUILD_NUMBER%'
                bat 'kubectl rollout status deployment/%K8S_DEPLOYMENT% --timeout=120s'
            }
        }

        stage('Verify Deployment') {
            steps {
                bat 'kubectl get deployments'
                bat 'kubectl get pods'
                bat 'kubectl get services'
            }
        }
    }

    post {
        success {
            echo 'CI/CD pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the Console Output for details.'
        }
    }
}
