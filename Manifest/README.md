# START K8S 
kubectl proxy --port=8001


Option 1 — Install with Helm (recommended) 
 
 # Helm pre commands 

helm repo add bitnami https://charts.bitnami.com/bitnami
 
helm repo update

 
# Add the ingress-nginx repo
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

# Install into ingress-nginx namespace
helm install ingress-nginx ingress-nginx/ingress-nginx \
  --namespace ingress-nginx --create-namespace 

Option 2 — Install with kubectl (quick manifest) 
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml




# Install POSTGRESQL
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

helm install postgres bitnami/postgresql \
  --set auth.username=admin \
  --set auth.password=admin \
  --set auth.database=groupstelar \
  --set persistence.size=1Gi
  
# Install PGADMIN
  
  helm install pgadmin bitnami/pgadmin \
  --set pgadminUsername=admin \
  --set pgadminPassword=admin \
  --set service.type=NodePort \
  --set service.nodePort=30088



# CONNECT INTO POSTGRESQL USING PGADMIN 
 kubectl get nodes -o wide

NAME             STATUS   ROLES           AGE   VERSION   INTERNAL-IP    EXTERNAL-IP   OS-IMAGE         KERNEL-VERSION                       CONTAINER-RUNTIME
docker-desktop   Ready    control-plane   13d   v1.32.2   192.168.65.3   <none>        Docker Desktop   5.15.167.4-microsoft-standard-WSL2   docker://28.3.2

http://192.168.65.3:30080


### HTTPS install 

# 1️ ⃣  Install cert-manager
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.14.4/cert-manager.yaml

# 2️ ⃣ Create a ClusterIssuer

apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: selfsigned-cluster-issuer
spec:
  selfSigned: {}

# 3 ️⃣ Generate TLS certificate (no openssl?)
apiVersion: cert-manager.io/v1
kind: Certificate
metadata:
  name: keycloak-tls
  namespace: keycloak
spec:
  dnsNames:
    - stayflex.keycloak.local
  secretName: keycloak-tls
  issuerRef:
    name: selfsigned-cluster-issuer
    kind: ClusterIssuer

# 4 ️⃣ Configure /etc/hosts
127.0.0.1 stayflex.keycloak.local

# 5 ️⃣ Create Keycloak Ingress using the cert
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: keycloak-ingress
  namespace: keycloak-ns
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
spec:
  ingressClassName: nginx
  tls:
    - hosts:
        - stayflex.keycloak.local
      secretName: keycloak-tls
  rules:
    - host: stayflex.keycloak.local
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: keycloak
                port:
                  number: 8080




# Given your Keycloak setup:

REALM: stayflex
Client ID: internal-api
AUTH FLOW: Standard flow
CLIENT AUTHENTICATOR: Client ID and Secret
CLIENT SECRET: znERfcJOPIcmuXrJYWPTGne4VKQrq8HY

You can get the bearer token by making a POST request to Keycloak’s token endpoint:

curl --location --request POST 'http://localhost:30080/realms/stayflex/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=internal-api' \
--data-urlencode 'client_secret=znERfcJOPIcmuXrJYWPTGne4VKQrq8HY' \
--data-urlencode 'username=jamesmedice' \
--data-urlencode 'password=tpm1234' \
--data-urlencode 'grant_type=password'

 

curl --location --request POST 'http://stayflex.keycloak.local/realms/stayflex/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=internal-api' \
--data-urlencode 'client_secret=znERfcJOPIcmuXrJYWPTGne4VKQrq8HY' \
--data-urlencode 'username=jamesmedice' \
--data-urlencode 'password=tpm1234' \
--data-urlencode 'grant_type=password'


curl --location --request POST 'http://192.168.253.18:30080/realms/stayflex/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=internal-api' \
--data-urlencode 'client_secret=znERfcJOPIcmuXrJYWPTGne4VKQrq8HY' \
--data-urlencode 'username=jamesmedice' \
--data-urlencode 'password=tpm1234' \
--data-urlencode 'grant_type=password'



curl  -v --request GET 'http://localhost/user/users/3fa85f64-5717-4562-b3fc-2c963f66afa6' \
--header "Accept: application/json" \
--header "Authorization: Bearer 



curl  -v --request GET 'http://localhost:32524/users/3fa85f64-5717-4562-b3fc-2c963f66afa6' \
--header "Accept: application/json" \
--header "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ1SGlwcXJISGhDdzVvMzNHa3B3UGxpRTJyZmQxUXJxOVlRYzVkVGxNdzcwIn0.eyJleHAiOjE3NTUyMDk3NjksImlhdCI6MTc1NTIwOTQ2OSwianRpIjoib25ydHJvOmNjZTExYWYzLTZlZmEtYTEyNy02YTQwLWEyODljNjM1ZjBlZCIsImlzcyI6Imh0dHA6Ly8xOTIuMTY4LjI1My4xODozMDA4MC9yZWFsbXMvc3RheWZsZXgiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNjlhMzBjODAtYjk3Yy00MzFlLWFiMTQtNmVmYjNmZjZiNDIzIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaW50ZXJuYWwtYXBpIiwic2lkIjoiOGFiMzQzZWQtMDhkYS00ZGY4LTk5NGQtNDIxMGNmYjM0NDZkIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1zdGF5ZmxleCIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJpbnRlcm5hbC1hcGkiOnsicm9sZXMiOlsiQURNSU4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoidGlhZ28gcHJhdGlzIG1lZGljZSIsInByZWZlcnJlZF91c2VybmFtZSI6ImphbWVzbWVkaWNlIiwiZ2l2ZW5fbmFtZSI6InRpYWdvIiwiZmFtaWx5X25hbWUiOiJwcmF0aXMgbWVkaWNlIiwiZW1haWwiOiJ0aWFnby5zbGxhdGVyQGdtYWlsLmNvbSJ9.YQvX90P6tl2mM-3-3ftcZn6PNtMZaMReX-ixHwJTAU4QGAC35Dxe6jYrk_BDXP69qW6uEe5h67yjy4Bw5scJmcquMBXgBWnOnrlJm1wVltDAX37WkiDbiN5g4B-TL8pNGd798L1tiFTFSS8N5T7U3I1zQH7lf4JWXRqIXhMBubYcYxLhy6o1sstKc1E-VEbmPKmspKwqAF4Ltw3TP5nvt2VoKAAQm-zv9YDKBhcib0PB_Apq5TKecDKvLef0hqXe36f8vy8WAJGp9VYrpnY5etoLnYkCW9Ep4HbUa5N7tVEUioD_198usT8beqaV0ajcuYLhUxfR87UEFIiO4Kd_rA"




curl  -v --request GET 'http://localhost/user-v1/actuator' \
--header "Accept: application/json" \
--header "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJwQzlOU2RXdEp0OE8zdzVVLTVELXFGRkwzanMxR0ZUTnQtYXRxOEFUZVJnIn0.eyJleHAiOjE3NTYyMDE0NDYsImlhdCI6MTc1NjIwMTE0NiwianRpIjoib25ydHJvOmZjOGQ3Mzg2LTMyOTktOGVkMC0yYzBiLWQ3NDhhOTFlNGMyYSIsImlzcyI6Imh0dHA6Ly8xMC4yNTQuMTguMjozMDA4MC9yZWFsbXMvc3RheWZsZXgiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiODk3MDE4MjYtY2IyYy00ZmFiLTljNzQtMjk4M2VmMWJkNTZlIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaW50ZXJuYWwtYXBpIiwic2lkIjoiNTE1NTFlOGQtYzc5Ny00ZGZlLWFhMDctMmIzODYxOWU4ZjYxIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1zdGF5ZmxleCIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJpbnRlcm5hbC1hcGkiOnsicm9sZXMiOlsiT1dORVIiLCJBRE1JTiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJ0aWFnbyBwcmF0aXMgbWVkaWNlIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidGlhZ28uc2xsYXRlckBnbWFpbC5jb20iLCJnaXZlbl9uYW1lIjoidGlhZ28iLCJmYW1pbHlfbmFtZSI6InByYXRpcyBtZWRpY2UiLCJlbWFpbCI6InRpYWdvLnNsbGF0ZXJAZ21haWwuY29tIn0.cyfJ7qqqbeU29x9oyk_LlRIaYnJjMJytxDZPyzE0pbQT4Toi8se1VIBeFBAbaDGZ-6632dNe2TMRljUBeoiqDawyukeSco774Ga7p45-jnModS_W8j3J3xBM_nX2SSxpyHu8SbzI-c3C_IJ2_TycP6Ktadhm7MhFLL0bd469kr_iwb7YWGsWouTMujEBfzf0y0TjD9PuAJ38QuK43EYy-m_hITfTYzYoAS9bekmZycFFGN1mhuz5iNtmmPZeoiC9crJtVIf7KbBcoZcpxr7mqsH5qxF2I8iGsdpkg6wtzsdGDxnvDc3EcZXmBu_ljKiV-eGrNZDm4623esL8WA10Sg"



curl  -v --request GET 'http://localhost/user-v1/users/3fa85f64-5717-4562-b3fc-2c963f66afa6' \
--header "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ1SGlwcXJISGhDdzVvMzNHa3B3UGxpRTJyZmQxUXJxOVlRYzVkVGxNdzcwIn0.eyJleHAiOjE3NTU1MTIwNTksImlhdCI6MTc1NTUxMTc1OSwianRpIjoib25ydHJvOjFlYmU4ZmI4LTE5NzctMWJkMy1mNzA3LTFhM2U1MzgxZmU5NyIsImlzcyI6Imh0dHA6Ly8xOTIuMTY4LjI1My4xODozMDA4MC9yZWFsbXMvc3RheWZsZXgiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNjlhMzBjODAtYjk3Yy00MzFlLWFiMTQtNmVmYjNmZjZiNDIzIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaW50ZXJuYWwtYXBpIiwic2lkIjoiMDZhMTMzMWItN2Y3My00OTNjLWE4MDAtMWVlMWVkY2ZlMTZhIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1zdGF5ZmxleCIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJpbnRlcm5hbC1hcGkiOnsicm9sZXMiOlsiQURNSU4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoidGlhZ28gcHJhdGlzIG1lZGljZSIsInByZWZlcnJlZF91c2VybmFtZSI6ImphbWVzbWVkaWNlIiwiZ2l2ZW5fbmFtZSI6InRpYWdvIiwiZmFtaWx5X25hbWUiOiJwcmF0aXMgbWVkaWNlIiwiZW1haWwiOiJ0aWFnby5zbGxhdGVyQGdtYWlsLmNvbSJ9.qh5nU_cIpywTy0PQk4ivlaWVCkyPcgoTvtPMKJa8fSe7Y0mVgNXbw5zzpBzzOMZmODUo7KL0Hp19EJSXwuDCoFHF14TANO6Glsl1AoINrD2cOvWR183zz4wakgAAtD8pt0yaXuB2V-1P7Uo7hmmWq6nYkmCTkBNwdcYadUSGCMa9fNJUM9mJ6_DxVXzWQ2VrAbaUY9XhTqhMHUGZuRB-aRgOc45vZm5FUTe_DZkOih8MUV_irkFI9SYX1c3OLL3whOAQiPqGWSFIQtBIhgyi4zvA1DR1tFFqWuF4lorL2GJGgTbd4vjHa9BxS-8YkDVfgAtSWLj9DwhiVPUUldypSg"





### GOOGLEAPIS

GET https://www.googleapis.com/books/v1/volumes?country=US&q=subject:java
1: sql
GET https://www.googleapis.com/books/v1/volumes?country=US&q=subject:sql
2: python
GET https://www.googleapis.com/books/v1/volumes?country=US&q=subject:python
done sending the requests


 
https://developers.google.com/oauthplayground/

nano values.json

{  "name": "qwiklabs-gcp-00-0f560c1bb3f1-bucket",
   "location": "us",
   "storageClass": "multi_regional"
}

Cloud Storage API V1.

  https://www.googleapis.com/auth/devstorage.full_control  .

 -->> Authorize APIs 
 
 -->> Click on Exchange authorization code  
 
 export OAUTH2_TOKEN=<YOUR_TOKEN>
 export PROJECT_ID=$(gcloud config get-value project)
 
	## CREATE BUCKET
 
 curl -X POST --data-binary @values.json \
    -H "Authorization: Bearer $OAUTH2_TOKEN" \
    -H "Content-Type: application/json" \
    "https://www.googleapis.com/storage/v1/b?project=$PROJECT_ID"
	
	
	
	## UPLOAD
	
 export OBJECT=/path-to-image
 export BUCKET_NAME=qwiklabs-gcp-00-0f560c1bb3f1-bucket 
 
 curl -X POST --data-binary @$OBJECT \
    -H "Authorization: Bearer $OAUTH2_TOKEN" \
    -H "Content-Type: image/png" \
    "https://www.googleapis.com/upload/storage/v1/b/$BUCKET_NAME/o?uploadType=media&name=demo-image"