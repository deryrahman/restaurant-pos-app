var coreService = "http://localhost:8080/restaurant"

$(document).ready(function(){
    // Category
    $.getJSON(coreService+"/categories", function (data) {
        var payload = data["payload"]
        var categories = []
        $.each(payload, function (key, val) {
            var id = "category-"+key
            var name = val["name"]
            categories.push(
                "<li id='"+id+"'>"+
                    "<a href='#"+id+"' data-toggle='tab'>"+
                    name+
                    "</a>"+
                "</li>"
            )
        })

        $("#nav-categories").append(categories)
    })
});