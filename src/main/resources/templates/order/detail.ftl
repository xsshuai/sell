<html>
    <#include "../common/head.ftl">
    <body>
        <div id="wrapper" class="toggled">
            <#--边栏sidebar-->
            <#include "../common/nav.ftl">
            <#--主要内容content-->
            <div id="page-content-wrapper">
                <div class="container">
                    <div class="row clearfix">
                        <div class="col-md-11 column">
                            <table class="table table-bordered table-condensed">
                                <thead>
                                <tr>
                                    <th>订单id</th>
                                    <th>订单总金额</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>${orderDTO.getOrderId()}</td>
                                    <td>${orderDTO.getOrderAmount()}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <#--订单详情表数据-->
                        <div class="col-md-11 column">
                            <table class="table table-bordered table-condensed">
                                <thead>
                                <tr>
                                    <th>商品id</th>
                                    <th>商品名称</th>
                                    <th>商品单价</th>
                                    <th>商品数量</th>
                                    <th>商品总价</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list orderDTO.getOrderDetailList() as orderDetail>

                                    <tr>
                                        <td>${orderDetail.productId}</td>
                                        <td>${orderDetail.productName}</td>
                                        <td>${orderDetail.productPrice}</td>
                                        <td>${orderDetail.productQuantity}</td>
                                        <td>${orderDetail.productPrice * orderDetail.productQuantity}</td>
                                    </tr>
                                </#list>
                                </tbody>
                            </table>
                        </div>
                        <div class="col-md-12 column">
                            <#if orderDTO.getOrderStatusEnum().message == "新订单" && orderDTO.getPayStatusEnum().message == "已支付">
                                <a href="/sell/seller/order/finish?orderId=${orderDTO.orderId}" type="button" class="btn btn-default btn-primary">完结订单</a>
                                <a href="/sell/seller/order/cancel?orderId=${orderDTO.orderId}" type="button" class="btn btn-default btn-danger">取消订单</a>
                            <#elseif orderDTO.getOrderStatusEnum().message == "新订单" && orderDTO.getPayStatusEnum().message == "未支付">
                                <a href="/sell/seller/order/cancel?orderId=${orderDTO.orderId}" type="button" class="btn btn-default btn-danger">取消订单</a>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>