<html>
    <head>
        <meta charset="utf-8">
        <title>错误提示</title>
        <link href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="col-md-12 column">
            <div class="alert alert-dismissable alert-warning">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <h4>
                    页面错误！
                </h4> <strong>${msg}</strong><a href="${url}" class="alert-link">3s后自动跳转</a>
            </div>
        </div>
    </body>
    <script>
        setTimeout('location.href="${url}"',3000)
    </script>
</html>