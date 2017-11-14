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