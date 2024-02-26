---
title: 部署指南
date: 2020-05-11 13:54:40
permalink: /pages/793dcb
article: false
---

::: warning 注意⚠️
1. 你需要至少一个mysql数据库
2. 你需要至少一个redis数据库
3. 你需要一个版本至少 kubernetes 1.29的集群(集群可选)

:::



## 单机部署(docker)
<code-group>
  <code-block title="docker拉取安装" active>

```bash
# clone the project
docker run -p 8080:8080 \
 -v  你的数据目录:/app\
  -e CONFIG_ISCLUSTERMODEENABLED=flase \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://192.168.0.254:3306/image_manage?userUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  -e SPRING_REDIS_HOST=192.168.0.254 \
  -e SPRING_REDIS_PASSWORD=123456 \
  wnzzer/image-manage:latest
# 这里配置数据挂载卷，mysql，数据库和redis数据库
```
> 仓库地址： <https://github.com/wnzzer/image-manage>


  </code-block>
</code-group>

::: tip

1. 由于springboot的配置替换策略，如果要替换更多的springboot参数配置也是可行的

:::

## 集群部署部署(k8s)
::: tip

1. 这是应用监控资源必须的组件，如果没有该组件，k8s metrics api将无法工作，image-manage将无法搜集pod资源信息

:::
1. 安装最metrics server：
```sh
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

2. 创建image-manage应用
```sh
wget  https://github.com/wnzzer/image-manage/releases/latest/download/image-manage-yaml
```
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: image-manage

---
# storeclass 声明，非常关键，用于动态分配卷
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: nfs-store-class
provisioner: image-manage-pv-provisioner  # 替换为实际的卷插件，我这里使用的是nfs的自动供应器，可以采用公用云或者其他pv自动供应器
reclaimPolicy: Delete
parameters:
  volumeSize: "1Gi"  # 应用存储图片的空间
  nfsServer: 192.168.0.254
  nfsPath: /volume1/nfs

---
# configMap k8s权限配置到英雄
apiVersion: v1
kind: ConfigMap
metadata:
  namespace: image-manage
  name: k8s-role-config
data:
  key1: ./admin.config

---

# 应用
apiVersion: apps/v1
kind: StatefulSet
metadata:
  namespace: image-manage
  name: image-manage
spec:
  serviceName: "image-manage-headless-service"
  replicas: 2
  selector:
    matchLabels:
      app: image-manage
  template:
    metadata:
      labels:
        app: image-manage
    spec:
      containers:
      - name: image-manage
        image: wnzzer/image-manage:latest
        ports: 
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: CONFIG_ISCLUSTERMODEENABLED
          value: "false"
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:mysql://192.168.0.254:3306/image_manage?userUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai"
        - name: SPRING_DATASOURCE_USERNAME
          value: "root"
        - name: SPRING_DATASOURCE_PASSWORD
          value: "123456"
        - name: SPRING_REDIS_HOST
          value: "192.168.0.254"
        - name: SPRING_REDIS_PASSWORD
          value: "123456"
        volumeMounts:
        - name: local-vol
          mountPath: "/app"
        - name: config-volume  # 挂载ConfigMap作为卷
          mountPath: "/etc/image-manage/config"  # 指定挂载ConfigMap的路径
      volumes:
      - name: config-volume  # 定义卷使用的ConfigMap
        configMap:
          name: k8s-admin-role-config  # 指定ConfigMap的名称
  volumeClaimTemplates:
  - metadata:
      name: local-vol
    spec:
      accessModes: [ "ReadWriteOnce" ]
      storageClassName: "nfs-store-class"
      resources:
        requests:
          storage: 1Gi

---
# 无头服务,用于应用的同步通信
apiVersion: v1
kind: Service
metadata:
  name: image-manage-headless-service
  namespace: image-manage
spec:
  clusterIP: None
  selector:
    app: image-manage
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080


---
# cluster service 用于输出api
apiVersion: v1
kind: Service
metadata:
  name: image-manage-cluster-service
  namespace: image-manage
spec:
  type: ClusterIP
  ports:
  - port: 8080
    targetPort: 8080
  selector:
    app: image-manage
```
```sh
kubectl apply -f image-manage.yaml
```

:::tip

  1. 这里同样需要把redis，mysql修改成自己的配置
  2. 这里需要有自己的pv供应器，由于是pv动态创建，所以需要搭配自动供应器给statusfulSet创建pv，示例中使用的是nfs
  3. image-manage 需要搭配 k8s 配置进行对k8s api的访问，这里实例使用的是admin.conf,使用configmap挂载到容器里，如果想要更细致的权限划分，请将权限配置至少给予 image-manage级别的权限。
  4. 创建的k8s用户文件请将命名为admin.conf,因为应用里指定了k8s配置文件为admin.conf，其他名称会无法读取。
:::

3. 访问

我们可以直接在k8s中部署nginx，反代 image-manage cluster ip，进行访问，
