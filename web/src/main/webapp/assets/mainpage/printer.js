$(document).ready(function () {
    var doc = new jsPDF("p", "pt", "a5");

    $("#btn-print").click(function () {
        doc.fromHTML($("#receipt-body")[0], 15, 15);
        doc.save(Date.now()+".pdf");
    })
});