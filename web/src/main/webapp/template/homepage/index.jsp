<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Homepage</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../../web/src/main/webapp/assets/style.css">
    <link rel="stylesheet" type="text/css" href="../../web/src/main/webapp/assets/custom.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" type="text/javascript" charset="utf-8" async defer></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js" type="text/javascript" charset="utf-8" async defer></script>
    <script> 
    $(document).ready(function(){
        $(".toggle-sidebar-in-main").click(function(){
            $("#sidebar").animate({width:'toggle'},'medium',function(){
                $("#inside-wrapper-sidebar").fadeToggle('fast');
            });
            $(".overlay").fadeToggle('slow');
        });
        $(".overlay, .toggle-sidebar-in-sidebar, #add-item").click(function(){
            $("#inside-wrapper-sidebar").fadeToggle(10,function(){
                $("#sidebar").animate({width:'toggle'},'medium');
                $(".overlay").fadeToggle('slow');
            });
        });
        $(".toggle-search").click(function(){
            $('.search-bar').css('position','absolute');
            $('.logo').fadeToggle('fast');
            $('.search-bar').fadeToggle('fast');
        });
    });
    </script>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-xs-11 col-sm-5 col-lg-4" id="sidebar">
                <div id="wrapper-sidebar">
                    <div id="inside-wrapper-sidebar">
                        <div class="row padding-default bg-main margin-bottom-default">
                            <div class="col-xs-10 col-sm-6">
                                <div class="page-header page-header-sidebar">
                                    <h3>Invoice #1234<br>
                                    <small class="white">Sat, 23/09/2017</small>
                                    </h3>
                                </div>
                            </div>
                            <div class="col-xs-2 col-sm-6">
                                <div class="icon-helper-page-header align-right">
                                    <div class="toggle-sidebar pointer toggle-sidebar-in-sidebar">
                                        <span class="glyphicon glyphicon-remove hidden-lg"></span>
                                    </div>
                                    <div id="member-auth">
                                        <a href="#">
                                            <span class="glyphicon glyphicon-user"></span><span class="hidden-xs"> New member</span>
                                        </a><br>
                                        <a href="#">
                                            <span class="glyphicon glyphicon-log-in"></span><span class="hidden-xs"> Login member</span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row padding-default">
                            <div class="col-xs-12 hidden-lg">
                                <a href="#" id="add-item"><span class="glyphicon glyphicon-plus"></span> Add item</a>
                            </div>
                        </div>
                        <div class="row padding-default">
                            <div id="invoice-item-list" class="col-xs-12">
                                <div class="invoice-item padding-v-default invoice-item-with-delete pointer row" data-toggle="modal" data-target="#delete-item-modal">
                                    <div class="col-xs-6 invoice-item-name">
                                        Ayam bakar kecap pedas
                                    </div>
                                    <div class="col-xs-2 invoice-item-count">
                                        2
                                    </div>
                                    <div class="col-xs-4 invoice-item-price align-right">
                                        <div class="row padding-default">
                                            Rp35000
                                        </div>
                                        <div class="row padding-default">
                                            Rp70000
                                        </div>
                                    </div>
                                </div>
                                <div class="invoice-item padding-v-default invoice-item-with-delete pointer row"  data-toggle="modal" data-target="#delete-item-modal">
                                    <div class="col-xs-6 invoice-item-name">
                                        Jus lemon
                                    </div>
                                    <div class="col-xs-2 invoice-item-count">
                                        2
                                    </div>
                                    <div class="col-xs-4 invoice-item-price align-right">
                                        <div class="row padding-default">
                                            Rp35000
                                        </div>
                                        <div class="row padding-default">
                                            Rp70000
                                        </div>
                                    </div>
                                </div>
                                <div class="invoice-item padding-v-default invoice-item-with-delete pointer row" data-toggle="modal" data-target="#delete-item-modal">
                                    <div class="col-xs-6 invoice-item-name">
                                        Greentea caffe
                                    </div>
                                    <div class="col-xs-2 invoice-item-count">
                                        2
                                    </div>
                                    <div class="col-xs-4 invoice-item-price align-right">
                                        <div class="row padding-default">
                                            Rp35000
                                        </div>
                                        <div class="row padding-default">
                                            Rp70000
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <hr class="full gray">
                        <div class="row padding-default">
                            <div id="invoice-subtotal" class="col-xs-12">
                                <div class="invoice-item row">
                                    <div class="col-xs-6 invoice-item-name">
                                        Subtotal
                                    </div>
                                    <div class="col-xs-2 invoice-item-count">
                                    </div>
                                    <div class="col-xs-4 invoice-item-price align-right">
                                        <div class="row padding-default">
                                            Rp210000
                                        </div>
                                    </div>
                                </div>
                                <div class="invoice-item row">
                                    <div class="col-xs-6 invoice-item-name">
                                        Tax
                                    </div>
                                    <div class="col-xs-2 invoice-item-count">
                                        5%
                                    </div>
                                    <div class="col-xs-4 invoice-item-price align-right">
                                        <div class="row padding-default">
                                            Rp10500
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <hr class="full gray">
                        <div class="row padding-default margin-bottom-default">
                            <div id="invoice-subtotal" class="col-xs-12">
                                <div class="invoice-item row">
                                    <div class="col-xs-6 invoice-item-name">
                                        Total
                                    </div>
                                    <div class="col-xs-2 invoice-item-count">
                                    </div>
                                    <div class="col-xs-4 invoice-item-price align-right">
                                        <div class="row padding-default">
                                            Rp220500
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row padding-default margin-bottom-default">
                            <div class="col-xs-12 col-sm-6 margin-bottom-default">
                                <button type="submit" class="btn btn-danger col-xs-12" data-toggle="modal" data-target="#delete-item-modal"><span class="glyphicon glyphicon-trash"></span> Remove All</button>
                            </div>
                            <div class="col-xs-12 col-sm-6 margin-bottom-default">
                                <button type="submit" class="btn btn-success col-xs-12" data-toggle="modal" data-target="#payment-modal">Payment</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 overlay">
            </div>
            <div class="col-xs-12 col-lg-8" id="main">
                <div id="wrapper-main">
                    <div class="row">
                        <div class="col-xs-12" id="header">
                            <header id="wrapper-header" class="bg-main">
                                <div class="row">
                                    <div class="col-xs-2 col-md-1 line-height-header align-right">
                                        <div class="toggle-search pointer padding-default">
                                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                                        </div>
                                    </div>
                                    <div class="col-xs-8 col-md-10">
                                        <div class="row">
                                            <div class="search-bar">
                                                <form>
                                                    <input type="text" name="s" placeholder="Search.." class="full">
                                                </form>
                                            </div>
                                            <div class="line-height-header align-center logo">
                                                LOGO
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-md-1 line-height-header align-left">
                                        <div class="toggle-sidebar pointer toggle-sidebar-in-main hidden-lg">
                                            <span class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span>
                                        </div>
                                    </div>
                                </div>
                            </header>
                            <div class="row">
                                <div class="col-xs-12">
                                    <nav class="navbar nav-tabs navbar-white-orange">
                                        <div class="container-fluid">
                                                <ul class="nav navbar-nav">
                                                    <li class="active"><a href="#all" data-toggle="tab">All</a></li>
                                                    <li><a href="#food" data-toggle="tab">Food</a></li>
                                                    <li><a href="#dessert" data-toggle="tab">Dessert</a></li>
                                                    <li><a href="#drink" data-toggle="tab">Drink</a></li>
                                                </ul>
                                        </div>
                                    </nav>
                                </div>
                            </div>
                        </div><!-- /header -->
                    </div>
                    <div id="wrapper" class="row padding-default">

                        <div class="tab-content">
                            <div class="tab-pane fade active in" id="all">
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap Pedas Sekali</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="food">
                                <div class="row padding-default margin-bottom-default">
                                    <div id="wrapper-category">
                                        <div class="col-xs-12">
                                            <div class="category-tag">
                                                All, Ayam, Ikan, Udang
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Ayam Bakar Kecap Pedas Sekali</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="dessert">
                                <div class="row padding-default margin-bottom-default">
                                    <div id="wrapper-category">
                                        <div class="col-xs-12">
                                            <div class="category-tag">
                                                All, Pudding, Buah, Pancake
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Puding mahal</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="drink">
                                <div class="row padding-default margin-bottom-default">
                                    <div id="wrapper-category">
                                        <div class="col-xs-12">
                                            <div class="category-tag">
                                                All, Soft drink, Jus, Caffe
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-3">
                                    <div class="item pointer">
                                        <div class="row padding-default">
                                            <div class="col-xs-8 col-sm-12">
                                                <h4 class="title-item">Greentea Caffe</h4>
                                            </div>
                                            <div class="col-xs-4 col-sm-12 align-right price-item">
                                                Rp35000
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Notification -->
    <div id="delete-item-modal" class="modal fade" role="dialog">
        <div class="modal-dialog modal-sm">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Are you sure delete this item?</h4>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Delete</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                </div>
            </div>

        </div>
    </div>
    <div id="payment-modal" class="modal fade" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Payment Process</h4>
                </div>
                <div class="modal-body">
                    <p>Payment process via :</p>
                    <ul>
                        <li>Cash</li>
                        <li>Credit</li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-success" data-dismiss="modal">Submit</button>
                </div>
            </div>

        </div>
    </div>
</body>
</html>