## chapter5
- 调整了spring-mvc.xml
- 配置了h2-console
- 添加了src/test/java
    - cn.niceabc.chapter5.IdentityServerTest.java
    - cn.niceabc.chapter5.UserAndGroupInUserTaskTest.java

## chapter6
- 连接字符串里的~表示home
    ```xml
    <property name="jdbcUrl" value="jdbc:h2:file:~/activiti-in-action-chapter6;AUTO_SERVER=TRUE" />
    ```
- 测试步骤
    ```text
    mvn antrun:run -Pinit-db
    mvn jetty:run
    ```