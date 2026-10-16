 ️⃣  Enable Kubernetes Dashboard


Docker Desktop usually includes the dashboard. If not:

kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml

kubectl create serviceaccount admin-user -n kubernetes-dashboard
kubectl create clusterrolebinding admin-user-binding \
  --clusterrole=cluster-admin \
  --serviceaccount=kubernetes-dashboard:admin-user
  
kubectl -n kubernetes-dashboard create token admin-user

 
 
kubectl proxy --port=8001 
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/kubernetes-dashboard/proxy/


in case of error 
 
 
kubectl port-forward -n kubernetes-dashboard svc/kubernetes-dashboard 8443:443
https://localhost:8443/
