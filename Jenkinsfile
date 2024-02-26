pipeline {
    agent any
    tools {
        jdk 'jdk17'
        maven 'maven3'
        dockerTool 'docker'
    }

    environment {
        SCANNER_HOME = tool 'sonar-scanner'
    }

    stages {
        stage('Git Checkout') {
            steps {
                git 'https://github.com/ChadenBA/RouterApp.git'
            }
        }
        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh """$SCANNER_HOME/bin/sonar-scanner \
                    -Dsonar.projectName=rouetrMangementApp \
                    -Dsonar.projectKey=rouetrMangementApp \
                    -Dsonar.java.binaries=.
                    """
                }
            }
        }
        stage('OWASP SCAN') {
            steps {
                dependencyCheck additionalArguments: '...scan ./', odcInstallation: 'DP'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Deploy to nexus') {
            steps {
                withMaven(globalMavenSettingsConfig: 'global-maven', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
                    sh 'mvn deploy'
                }
                echo "Hello, world!"
            }
        }
        stage('Build & Tag docker image') {
            steps {
                script {
                    withDockerRegistry(credentialsId: '4376e9ed-1a1f-4e86-88dd-7d8e5ff95d9d', toolName: 'docker') {
                        sh "docker build -t routerapp:latest -f /var/lib/jenkins/workspace/RouterApp/Dockerfile ."
                    }
                }
            }
        }
        stage('Trivy Scan') {
            steps {
                sh 'trivy  --timeout 5m image routerapp:latest > trivy-report.txt '
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    withDockerRegistry(credentialsId: '4376e9ed-1a1f-4e86-88dd-7d8e5ff95d9d', toolName: 'docker') {
                        sh "docker tag routerapp:latest chaden13/routerapp:latest"
                        sh "docker push chaden13/routerapp:latest"
                    }
                }
            }
        }
        stage('Kubernetes deploy') {
            steps {
                withKubeConfig(credentialsId: 'k8-token', namespace: 'routerapp', restrictKubeConfigAccess: false, serverUrl: 'https://192.168.1.56:6443') {
                    sh "kubectl apply -f deploymentservice.yml  -n routerapp "
                    sh "kubectl get svc -n routerapp"
                } 
            }
        }
    }
}
