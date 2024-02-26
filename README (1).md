
# Router Management Application with Pipeline Integration


In this mini-class project, we have developed a Router Management application. This application not only performs CRUD operations on routers but also introduces users to the complexities of managing Customer Premises Equipment (CPE) through a remote Configuration and Monitoring server.


## Purpose
The project aims to develop a Router Management Application with integrated Jenkins Pipeline automation. It facilitates CRUD operations on routers and explores CPE management intricacies. The pipeline streamlines development by automating code compilation, testing, and deployment, ensuring continuous integration, efficient deployment, and transparency. This combination of robust application development and automation offers valuable insights into modern software development practices.
## Installation

Install my-project with npm

```bash
  git clone https://github.com/YourUsername/RouterManagementApp.git
  cd RouterManagementApp
  mvn compile
```
    
## Running Tests

To run tests, run the following command

```bash
mvn test
```


## Kubernetes  Install 


```bash
apt-get update -y
apt-get install docker.io -y
service docker restart
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
echo "deb http://apt.kubernetes.io/ kubernetes-xenial main" >/etc/apt/sources.list.d/kubernetes.list
apt-get update
apt install kubeadm=1.20.0-00 kubectl=1.20.0-00 kubelet=1.20.0-00 -y

kubeadm init --pod-network-cidr=192.168.0.0/16

mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

kubectl apply -f https://raw.githubusercontent.com/projectcalico/calico/v3.25.1/manifests/calico.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.49.0/deploy/static/provider/baremetal/deploy.yaml
```

## Kubernetes Configuration

* serviceaccount.yml
This YAML configuration file defines a RoleBinding named `app-rolebinding` within the `routerapp` namespace. It binds the role `my-role` to the service account `jenkins`. This RoleBinding grants permissions to the Jenkins service account to interact with Kubernetes resources within the specified namespace.

```bash
 apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: app-rolebinding
  namespace: routerapp
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: my-role
subjects:
- namespace: routerapp
  kind: ServiceAccount
  name: jenkins
```
* role.yml

This YAML configuration file defines a Role named my-role within the routerapp namespace. It specifies the permissions granted to the role, allowing actions such as listing, watching, creating, updating, and deleting various Kubernetes resources within the namespace.


```bash
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: my-role
  namespace: routerapp
rules:
- apiGroups:
  - ""
  - apps
  - autoscaling
  - batch
  - policy
  - rbac.authorization.k8s.io
  resources:
  - pods
  - services
  - componentstatuses
  - daemonsets
  - deployments
  - events
  - endpoints
  - ingresses
  - jobs
  - namespaces
  - nodes
  - persistentvolumes
  - replicasets
  - services
  - serviceaccounts
  verbs: ["get", "list", "watch", "create", "update", "patch", "delete"]

```
* secret.yml
This YAML configuration file defines a Secret named mysecret within the Kubernetes cluster. It is annotated with the kubernetes.io/service-account.name: jenkins, indicating that it is associated with the jenkins service account. This Secret is used to securely store the service account token.
```bash
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
  annotations:
    kubernetes.io/service-account.name: jenkins
type: kubernetes.io/service-account-token
```

* service.yml
This YAML configuration file defines a ServiceAccount named jenkins within the routerapp namespace. It provides Jenkins with the necessary permissions to interact with Kubernetes resources within the specified namespace.
```bash
apiVersion: v1
kind: ServiceAccount
metadata:
  name: jenkins
  namespace: routerapp

```
## Jenkins Pipeline Steps

The deployment of the Router Management Application is facilitated through a Jenkins Pipeline. The pipeline automates various stages of the development process, ensuring seamless integration, testing, and deployment. Below are the steps involved in the Jenkins Pipeline:

1. **Git Checkout**: This stage checks out the latest code from the Git repository, ensuring that the pipeline operates on the most up-to-date codebase.

2. **Compile**: The application code is compiled to ensure that it is free of syntax errors and can be executed properly.

3. **Unit Test**: The unit tests are executed to verify the functionality of individual components of the application.

4. **SonarQube Analysis**: The code is analyzed using SonarQube to identify potential code smells, bugs, and security vulnerabilities.

5. **OWASP Scan**: This stage performs a security scan using OWASP Dependency Check to detect any vulnerabilities in the application dependencies.

6. **Build**: The application is built, resulting in an executable artifact that can be deployed.

7. **Deploy to Nexus**: The built artifact is deployed to the Nexus repository manager for version control and artifact management.

8. **Build & Tag Docker Image**: This stage builds and tags a Docker image for the application, ensuring consistency and portability across environments.

9. **Trivy Scan**: A vulnerability scan is performed on the Docker image using Trivy to detect any security vulnerabilities.

10. **Docker Push**: The Docker image is pushed to a Docker registry, making it accessible for deployment.

11. **Kubernetes Deploy**: The application is deployed to a Kubernetes cluster, ensuring scalability and high availability.

---

## Screenshots

![App Screenshot](https://res.cloudinary.com/dxzlm2qqz/image/upload/v1708971367/Screenshot_from_2024-02-25_21-17-27_hlkrou.png)


![App Screenshot](https://res.cloudinary.com/dxzlm2qqz/image/upload/v1708971552/Screenshot_from_2024-02-23_23-18-05_od8wuz.png)


## Tech Stack

The Router Management Application is built using the following technologies and tools:

Java, Maven, JUnit, SonarQube, OWASP Dependency Check, Docker, Trivy,Kubernetes, Jenkins, Nexus

## License
This project is licensed under the MIT License.

Feel free to contribute and open issues for any improvements or bugs.