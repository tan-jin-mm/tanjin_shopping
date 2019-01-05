<%--
  Created by IntelliJ IDEA.
  User: Jin
  Date: 2019/1/5
  Time: 16:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图片上传</title>
</head>
<body>
    <form action="/shopping//manage/product/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file"/>
        <input type="submit" value="图片上传"/>
    </form>
</body>
</html>
