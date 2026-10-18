# Student Complaint Management System

A beginner-friendly Java Spring Boot web app intended for a Git/GitHub, Maven, Jenkins, Docker, and Kubernetes DevOps project.

## Features
- Submit a student complaint with name, category, and description.
- View complaints on a dashboard.
- Change status between Pending, In Progress, and Resolved.
- Summary counts for total, pending, and resolved complaints.
- Dockerfile, Jenkinsfile, and Kubernetes manifests are included.

## Requirements
- JDK 21
- Maven 3.9+ (or use a Maven wrapper if you add one)
- Docker Desktop for container work
- kubectl and a running local Kubernetes cluster for Kubernetes deployment
- Jenkins with a configured JDK named `JDK21`, Maven named `Maven`, and access to Docker and kubectl for the included pipeline

## Run locally on Windows
Extract this ZIP into a **new folder** (do not merge it with the old project folder). Open PowerShell in the extracted `StudentComplaintSystem` folder:

```powershell
java -version
mvn -version
mvn spring-boot:run
```

If port 8080 is already in use, start on port 8081 instead:

```powershell
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Then open http://localhost:8081.

Open http://localhost:8080.

Run tests:

```powershell
mvn test
```

Package the app:

```powershell
mvn clean package
```

## Run using Docker
Build the image:

```powershell
docker build -t student-complaint-system:latest .
```

Run it:

```powershell
docker run --rm -p 8080:8080 student-complaint-system:latest
```

Open http://localhost:8080.

## Deploy to Kubernetes
The local Kubernetes cluster must be enabled and able to access the image. With Docker Desktop Kubernetes, build the image locally first:

```powershell
docker build -t student-complaint-system:latest .
kubectl apply -f k8s/
kubectl rollout status deployment/student-complaint-system
kubectl get pods
kubectl get services
```

Open http://localhost:30080 if your Kubernetes setup exposes NodePort on localhost.

If using a remote Kubernetes cluster, push the image to a container registry and change `image:` in `k8s/deployment.yaml` to the registry image path before applying.

## Jenkins notes
The included Jenkinsfile is a starting point for a Windows Jenkins agent (`bat` steps). Configure Jenkins global tools named exactly `JDK21` and `Maven`. The agent must have Docker and kubectl installed, access to the target Kubernetes cluster, and permission to deploy. If your Jenkins agent runs Linux, replace `bat` with `sh` and adapt tool configuration.

## Important note about data
This starter version stores complaints in memory. Sample complaints are included, and newly submitted complaints are lost when the application restarts. It is for a coursework/demo project, not production use. There is no authentication yet; the status update control is exposed on the dashboard for demonstration purposes.
