$(document).ready(function() {
    // jQuery code




    /* ///////////////////////////////////////

    THESE FOLLOWING SCRIPTS ONLY FOR BASIC USAGE,
    For sliders, interactions and other

    */ ///////////////////////////////////////


    //////////////////////// Prevent closing from click inside dropdown
    $(document).on('click', '.dropdown-menu', function (e) {
        e.stopPropagation();
    });

    //////////////////////// Bootstrap tooltip
    if($('[data-toggle="tooltip"]').length>0) {  // check if element exists
        $('[data-toggle="tooltip"]').tooltip()
    } // end if


});


let token = document.getElementById('_csrf').getAttribute('content');
//var token = $("input[name='_csrf']").val();
let header = document.getElementById('_csrf_header').getAttribute('content');



let page=0;
let i;


/*	$("#search").keyup(function() {

		$('#searchform').attr("action", "/search/" + $(this).val())
	});*/

let end = false;
let src = "";
let el;
let timer1;
let timer2;
let timer3;
let timer4;
function exitm(el){

    clearTimeout(timer1);
    clearTimeout(timer2);
    clearTimeout(timer3);
    clearTimeout(timer4);
    el.setAttribute("src",src);
}


function enterm(e) {


    src = e.getAttribute('src');


    timer1=  setTimeout(function () {

        e.setAttribute('src', e.nextElementSibling.getAttribute('src'));
        timer2= setTimeout(function () {


            e.setAttribute('src', e.nextElementSibling.nextElementSibling.getAttribute('src'));
            timer3= setTimeout(function () {

                e.setAttribute('src', e.nextElementSibling.nextElementSibling.nextElementSibling.getAttribute('src'));
            }, 1000);

        }, 1000);




    }, 1000);


};

/*	$('#quantity').val($("#selectQt option:selected" ).val())
	$("#selectQt").change(function() {
		$('#quantity').val($("#selectQt option:selected" ).val())
		var prodname=$("#pname").text();
		var qt=$("#quantity").val();
		$('#buyLink').attr("href","/checkout?prod="+prodname+"&qt="+qt)
	});*/

function  changeImage(e){
    $('#img').attr("src",e.getAttribute("src"));
}
function onPaged(pg){
    let cat=$(".activeCat").text()!==undefined?$(".activeCat").text():$(".catsmenu").text();
    let view=$(".myview.active").attr("view");
    let sort= $("#dropdownMenuButton").val();
    let max=$("#max").val();
    let min=$("#min").val();
    let search=$("#search").val();
 /*   if(cat!==undefined && cat!=="")
        window.location.href="/category/"+cat+"/SortBy/"+sort+"/page/"+pg;
    else*/
        window.location.href="/?sort="+sort+"&cat="+cat+"&page="+pg+"&view="+view+"&max="+max+"&min="+min+"&search="+search;

}

$(".fr1").submit(function(event){
    // Stop form from submitting normally
    event.preventDefault();
    /* Serialize the submitted form control values to be sent to the web server with the request */
    //var formValues = $(this).serialize();
    let form= $(this);
    let qt=$("#selectQt option:selected" ).val();
    url=qt!==undefined?'/api/item?quantity='+qt:'/api/item';
    let prodName = form.find("input[name='prodName']").val();
    $("#total").text(qt*parseInt($("#price").text()));
    $.ajax({
        type: "POST",
        url:url,
        dataType:'json',
        contentType:'application/json',
        data:prodName,
        beforeSend: function( xhr ) {
            xhr.setRequestHeader(header, token);
        },
        success:function (r) {
            if(!form.find("#edit").attr("hidden"))
            {
                $("#cartNum").text(parseInt($("#cartNum").text())+1);
                form.find("#edit").attr("hidden",true);
                form.find("#remove").attr("hidden",false);
            }
            else
            {
                $("#cartNum").text(parseInt($("#cartNum").text())-1);
                form.find("#edit").attr("hidden",false);
                form.find("#remove").attr("hidden",true);
            }

        },
        error:function (r) {
            if(!form.find("#edit").attr("hidden"))
            {
                $("#cartNum").text(parseInt($("#cartNum").text())+1);
                form.find("#edit").attr("hidden",true);
                form.find("#remove").attr("hidden",false);
            }
            else
            {
                $("#cartNum").text(parseInt($("#cartNum").text())-1);
                form.find("#edit").attr("hidden",false);
                form.find("#remove").attr("hidden",true);
            }
        },
        //data: JSON.stringify(url)
    });
    // Send the form data using post

});

