# Student Complaint Management System

A web-based application built using **Spring Boot** that allows students to submit and track complaints while administrators manage complaints and update their statuses. The project demonstrates a DevOps workflow using **Maven, Docker, Jenkins, and Kubernetes**.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Prerequisites](#prerequisites)
- [Run Locally](#run-locally)
- [Demo Credentials](#demo-credentials)
- [Docker Setup](#docker-setup)
- [Kubernetes Deployment](#kubernetes-deployment)
- [Jenkins CI/CD Pipeline](#jenkins-cicd-pipeline)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Limitations](#limitations)
- [Future Enhancements](#future-enhancements)
- [Author](#author)

## Project Overview

The Student Complaint Management System simplifies the process of reporting and managing student complaints. Students can submit complaints under categories such as Classroom and track their progress. Administrators can view submitted complaints and update their statuses.

The project also demonstrates an automated build and deployment workflow using Jenkins, Docker, and Kubernetes.

## Features

### Student
- Log in to the application.
- Submit complaints.
- Select a complaint category, including Classroom.
- View personal complaints and their statuses.

### Administrator
- Log in with administrator access.
- View complaints submitted by students.
- Update complaint statuses.

### DevOps
- Maven-based build and test workflow.
- Docker image creation.
- Jenkins pipeline automation.
- Kubernetes deployment and service configuration.
- Application rollout verification.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 3.5.6 | Backend application framework |
| Spring Security | Authentication and authorization |
| Thymeleaf | Server-rendered web pages |
| Maven | Build and dependency management |
| Docker | Containerization |
| Jenkins | CI/CD automation |
| Kubernetes | Container orchestration |
| Git and GitHub | Version control and source hosting |
| VS Code | Development environment |

## System Architecture

```text
Developer
   |
   v
GitHub Repository
   |
   v
Jenkins CI/CD Pipeline
   |
   +--> Build and Test
   |
   +--> Build Application
   |
   +--> Build Docker Image
   |
   +--> Verify Docker Image
   |
   +--> Deploy to Kubernetes
   |
   v
Kubernetes Deployment
   |
   v
Student Complaint Management System
   |
   v
Student / Administrator
```

## Prerequisites

Install the following tools before running the project:

- Java Development Kit (JDK) 21
- Apache Maven, or use the included Maven Wrapper if available
- Git
- Docker Desktop
- Kubernetes enabled in Docker Desktop (for Kubernetes deployment)
- Jenkins (for the CI/CD pipeline)
- kubectl (Kubernetes command-line tool)

Verify the installations:

```powershell
java -version
mvn -version
git --version
docker --version
kubectl version --client
```

## Run Locally

### 1. Clone the repository

```powershell
git clone https://github.com/AkshayaSindhu/DevOpsProject.git
cd DevOpsProject
```

If you already have the project locally, navigate to your existing project directory instead.

### 2. Build and test

```powershell
mvn clean test
```

### 3. Start the application

```powershell
mvn spring-boot:run
```

The application is configured to run on port `8084`.

Open the login page:

**http://localhost:8084/login**

To stop the application, press `Ctrl + C` in the terminal.

## Demo Credentials

| Role | Username | Password |
|---|---|---|
| Student | `student` | `Student123!` |
| Administrator | `admin` | `Admin123!` |

These are demonstration credentials for the current development setup. Change them before any public or production deployment.

## Docker Setup

The project includes a `Dockerfile` for packaging the Spring Boot application into a container image.

### 1. Build the application

```powershell
mvn clean package
```

### 2. Build the Docker image

Run this command from the project root, where the `Dockerfile` is located:

```powershell
docker build -t student-complaint-system:latest .
```

### 3. Run the container

The application listens on port `8084` inside the container:

```powershell
docker run --name student-complaint-system -p 8084:8084 student-complaint-system:latest
```

Open:

**http://localhost:8084/login**

To stop and remove the container:

```powershell
docker stop student-complaint-system
docker rm student-complaint-system
```

If a container with that name already exists, remove the old container before creating another one.

## Kubernetes Deployment

The Kubernetes manifests are stored in the `k8s/` directory:

- `k8s/deployment.yaml`
- `k8s/service.yaml`

The application listens on container port `8084`. The Service exposes port `8080` internally and uses NodePort `30083` for external access, subject to the local Kubernetes environment.

### 1. Enable Kubernetes

Open Docker Desktop and ensure Kubernetes is enabled and running.

Verify the cluster:

```powershell
kubectl get nodes
```

### 2. Build the Docker image

```powershell
mvn clean package
docker build -t student-complaint-system:latest .
```

For a local Docker Desktop Kubernetes cluster, ensure the image is available to the cluster and that the image name in `k8s/deployment.yaml` matches the image you built.

### 3. Apply the manifests

Run these commands from the project root:

```powershell
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

### 4. Verify the deployment

```powershell
kubectl get deployments
kubectl get pods
kubectl get services
```

Wait for the rollout to finish:

```powershell
kubectl rollout status deployment/student-complaint-system
```

Inspect the Service configuration:

```powershell
kubectl describe service student-complaint-service
```

Confirm that the Service target port is `8084`, matching the application's listening port.

### 5. Access the application

For local testing, port-forward the Kubernetes Service:

```powershell
kubectl port-forward service/student-complaint-service 8085:8080
```

Keep the terminal running and open:

**http://localhost:8085/login**

Press `Ctrl + C` to stop port forwarding.

### 6. View application logs

```powershell
kubectl logs deployment/student-complaint-system
```

To inspect rollout history:

```powershell
kubectl rollout history deployment/student-complaint-system
```

**Important:** The Docker image must be available to the Kubernetes cluster. If the image changes, rebuild it and ensure the cluster uses the updated image before deploying.

## Jenkins CI/CD Pipeline

The project uses Jenkins to automate the build, verification, containerization, and Kubernetes deployment workflow.

### Pipeline stages

1. **Checkout:** Retrieve the source code from GitHub.
2. **Build and Test:** Compile the application and execute tests.
3. **Build Application:** Package the Spring Boot application.
4. **Build Docker Image:** Create the container image.
5. **Verify Docker Image:** Check the built image.
6. **Deploy to Kubernetes:** Apply the deployment workflow.
7. **Verify Deployment:** Inspect Kubernetes deployments, pods, and services.

### Run the pipeline

1. Start Jenkins and ensure the required tools and Kubernetes access are configured.
2. Open the Jenkins dashboard.
3. Select the job `StudentComplaintSystem-CI`.
4. Click **Build Now**.
5. Open the build's **Console Output**.
6. Confirm that the pipeline finishes with `Finished: SUCCESS`.

A successful pipeline indicates that the configured pipeline steps completed. Verify that the application is reachable and the student and administrator workflows operate correctly after deployment.

## Project Structure

```text
StudentComplaintSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/studentcomplaintsystem/
│   │   │       ├── Complaint.java
│   │   │       ├── ComplaintController.java
│   │   │       ├── LoginController.java
│   │   │       ├── RegistrationController.java
│   │   │       ├── SecurityConfig.java
│   │   │       └── StudentAccount.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── index.html
│   │           ├── login.html
│   │           └── register.html
├── k8s/
│   ├── deployment.yaml
│   └── service.yaml
├── Dockerfile
├── pom.xml
└── README.md
```

*The tree above highlights the main files. Actual package contents and template names may differ as the project evolves.*

## Troubleshooting

### Application does not start

Check that Java 21 is installed and that port `8084` is available.

```powershell
java -version
```

### Docker build fails

Check that the `Dockerfile` is in the project root and run:

```powershell
mvn clean package
docker build -t student-complaint-system:latest .
```

### Kubernetes pod is not running

Inspect the pod status and logs:

```powershell
kubectl get pods
kubectl describe pod <pod-name>
kubectl logs deployment/student-complaint-system
```

Replace `<pod-name>` with the actual pod name.

### Login page is unreachable

Check the application logs and Kubernetes Service target port:

```powershell
kubectl describe service student-complaint-service
kubectl get endpoints student-complaint-service
```

The Service's `targetPort` must match the port on which Spring Boot is listening.

For local Kubernetes access, try port forwarding:

```powershell
kubectl port-forward service/student-complaint-service 8085:8080
```

Then open `http://localhost:8085/login`.

### Jenkins pipeline fails

Review the failed stage in the Jenkins Console Output. Verify GitHub access, Maven and Docker availability, Kubernetes cluster connectivity, and the correctness of the manifests.

## Limitations

- Complaint and registration data are stored in memory in the current implementation and are lost when the application restarts.
- Demo credentials are intended for development and testing only.
- The current setup is designed for a local demonstration rather than a hardened production environment.

## Future Enhancements

- Add persistent storage using MySQL or PostgreSQL.
- Add email notifications for complaint status updates.
- Add complaint priorities, timestamps, and search filters.
- Introduce environment-specific configuration and secret management.
- Add automated integration and end-to-end tests.
- Publish Docker images to a container registry.
- Add monitoring, logging, and production deployment practices.

## Author

**Akshaya Sindhu**

GitHub: [AkshayaSindhu](https://github.com/AkshayaSindhu)

Project repository: [Student Complaint Management System](https://github.com/AkshayaSindhu/DevOpsProject)

---


