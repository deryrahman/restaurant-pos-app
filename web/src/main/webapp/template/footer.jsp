    <!-- Scripts -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

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
</html>